package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class HomeActivity extends AppCompatActivity {

    ListView walkList;
    FloatingActionButton addFab;
    ArrayList<Walk> walkArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        walkList = findViewById(R.id.list_walks);
        addFab =   findViewById(R.id.addFab);

        //OnClickListener if one of the list items is clicked
        walkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                //create intent and bundle for View Activity
                Intent viewIntent= new Intent(HomeActivity.this, ViewActivity.class);
                Bundle viewBundle = new Bundle();

                //put the rest into a bundle so that we can use default values if required
                viewBundle.putInt("eventId", walkArrayList.get(i).getEventId());
                viewBundle.putString("eventName", walkArrayList.get(i).getEventName());
                viewBundle.putString("eventDatetime", walkArrayList.get(i).getEventDatetime());
                viewBundle.putString("eventLocation", walkArrayList.get(i).getEventLocation());
                viewBundle.putString("eventImageURL", walkArrayList.get(i).getEventImageURL());
                viewBundle.putDouble("eventCoordinateLang", walkArrayList.get(i).getEventCoordinateLat());
                viewBundle.putDouble("eventCoordinateLong", walkArrayList.get(i).getEventCoordinateLong());
                viewBundle.putString("eventDescription", walkArrayList.get(i).getEventDescription());
                viewBundle.putString("eventWeather", walkArrayList.get(i).getEventWeather());

                //add bundle to intent
                viewIntent.putExtra("bundle", viewBundle);

                //start activity with intent information
                startActivity(viewIntent);

            }
        });


        //Create click listener for add button
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent creation for CREATE Activity
                Intent createIntent= new Intent(HomeActivity.this, CreateActivity.class);
                startActivity(createIntent);
            }
        });


    }


    @Override
    protected void onResume() {
        super.onResume();

        //array containing all walk items
        walkArrayList = new ArrayList<>();

        //[Temp] method to populate array
        retrieveData();

        //sort walks
        sortData();

        //create Adapter instance
        WalkListAdapter walkListAdapter = new WalkListAdapter(this, R.layout.listview_item, walkArrayList);

        //apply the adapter to the walk ArrayList
        walkList.setAdapter(walkListAdapter);
    }

    /**
     * Method to populate the list of walks
     * Currently it is updated manually, future work requires it to be drawn from database
     */
    private void retrieveData() {


        Double currentLat  =  44.637386;
        Double currentLong = -63.587347;

        //Sample walks for testing
        Walk walk1 = new Walk(currentLat,currentLong,44.647824,-63.578754,1,"The Fats and the Furious","20-Apr-2019 14:00:00", "Citadel Hill Entrance","https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1538172046-woman-walking-on-a-path-royalty-free-image-503818820-1538171480.jpg");
        Walk walk2 = new Walk(currentLat,currentLong,44.640658,-63.573399,2,"The Mean Elder Machine","19-Apr-2019 05:00:00", "Elder Home #2","");
        Walk walk3 = new Walk(currentLat,currentLong,44.652321,-63.606486,3,"Mommy Group","19-Apr-2019 15:00:00", "Happy Day Care","https://www.sbs.com.au/topics/sites/sbs.com.au.topics/files/styles/full/public/gettyimages-78616903.jpg?itok=vpRKMrOO&mtime=1502951087");
        Walk walk4 = new Walk(currentLat,currentLong,44.624898,-63.564307,4,"Peddle to the Metal Stroller Club","19-Apr-2019 16:00:00","Point Pleasant Park Entrance","https://ak.jogurucdn.com/media/image/p25/place-2015-02-9-6-Pointpleasantpark00f1add88e3f735075966fe98d15cee6.jpg");
        Walk walk5 = new Walk(currentLat,currentLong,44.642293,-63.579871,5,"Fathers Unite Walk","20-Apr-2019 19:00:00","Public Gardens Gate","https://ak.jogurucdn.com/media/image/p25/place-2015-02-9-6-Halifaxpublicgardens4b3d0c8a233060473e9c85eb17d3a2d2.jpg");
        Walk walk6 = new Walk(currentLat,currentLong,44.637242,-63.590771,6,"The Nerd Herd","18-Apr-2019 13:00:00","Killam Library - Dalhousie university","https://cdn.dal.ca/content/dam/dalhousie/images/campus-maps/killam.jpg.lt_3037db728a38513ccb92db303fa8b758.res/killam.jpg");
        Walk walk7 = new Walk(currentLat,currentLong,44.643044,-63.577260,7,"Jittery Coffee Junkies","19-Apr-2019 17:00:00", "Tim Hortons Spring Garden Road","http://rcsinc.ca/wp-content/uploads/2015/05/TimsSgRd-2.jpg");


        //adding test walks to the array list
        walkArrayList.add(walk1);
        //Log.d("HomeActivity", "Walk: " + walk1.getEventName() + " Distance to user: " + walk1.getDistanceToUser());
        walkArrayList.add(walk2);
        //Log.d("HomeActivity", "Walk: " + walk2.getEventName() + " Distance to user: " + walk2.getDistanceToUser());
        walkArrayList.add(walk3);
        //Log.d("HomeActivity", "Walk: " + walk3.getEventName() + " Distance to user: " + walk3.getDistanceToUser());
        walkArrayList.add(walk4);
        //Log.d("HomeActivity", "Walk: " + walk4.getEventName() + " Distance to user: " + walk4.getDistanceToUser());
        walkArrayList.add(walk5);
        //Log.d("HomeActivity", "Walk: " + walk5.getEventName() + " Distance to user: " + walk5.getDistanceToUser());
        walkArrayList.add(walk6);
        //Log.d("HomeActivity", "Walk: " + walk6.getEventName() + " Distance to user: " + walk6.getDistanceToUser());
        walkArrayList.add(walk7);
        //Log.d("HomeActivity", "Walk: " + walk7.getEventName() + " Distance to user: " + walk7.getDistanceToUser());

    }

    /**
     * Method that sortes the walkArrayList using the GPS coordinates
     */
    private void sortData(){

        Collections.sort(walkArrayList, new Comparator<Walk>() {
            @Override
            public int compare(Walk walk1, Walk walk2) {

                return walk1.getDistanceToUser().compareTo(walk2.getDistanceToUser());
            }
        });


    }




}
