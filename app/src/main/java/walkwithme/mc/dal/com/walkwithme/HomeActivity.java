package walkwithme.mc.dal.com.walkwithme;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Activity mActivty;

    ListView walkList;
    FloatingActionButton addFab;

    ArrayList<Walk> walkArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mActivty = this;
        walkList = findViewById(R.id.list_walks);
        addFab = findViewById(R.id.addFab);


        //array containing all walk items
        walkArrayList = new ArrayList<>();

        //[Temp] method to populate array
        retrieveData();

        //sort walks



        //create Adapter instance
        WalkListAdapter walkListAdapter = new WalkListAdapter(this, R.layout.listview_item, walkArrayList);

        //apply the adapter to the walk ArrayList
        walkList.setAdapter(walkListAdapter);


        //OnClickListener if one of the list items is clicked
        walkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                Toast.makeText(HomeActivity.this, "clicked item: " + i + " "+ walkArrayList.get(i).toString(), Toast.LENGTH_LONG).show();

            }
        });


        //Create click listener for add button
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Intent creation for View Activity
                //Intent detailsIntent= new Intent(HomeActivity.this,ViewActivity.class);
                //detailsIntent.putExtra("event", result);
                //startActivity(detailsIntent);
            }
        });


    }

    //Temp method to populate the list of walks
    private void retrieveData() {

        //Sample walks for testing
        Walk walk1 = new Walk("The Fats and the Furious","20-Apr-2019 14:00:00", "Citadel Hill Entrance","https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1538172046-woman-walking-on-a-path-royalty-free-image-503818820-1538171480.jpg");
        Walk walk2 = new Walk("The Mean Elder Machine","19-Apr-2019 05:00:00", "Elder Home #2","");
        Walk walk3 = new Walk("Mommy Group","19-Apr-2019 15:00:00", "Happy Day Care","https://www.sbs.com.au/topics/sites/sbs.com.au.topics/files/styles/full/public/gettyimages-78616903.jpg?itok=vpRKMrOO&mtime=1502951087");
        Walk walk4 = new Walk("Peddle to the Metal Stroller Club","19-Apr-2019 16:00:00","Point Pleasant Park Entrance","https://ak.jogurucdn.com/media/image/p25/place-2015-02-9-6-Pointpleasantpark00f1add88e3f735075966fe98d15cee6.jpg");
        Walk walk5 = new Walk("Fathers Unite Walk","20-Apr-2019 19:00:00","Public Gardens Gate","https://ak.jogurucdn.com/media/image/p25/place-2015-02-9-6-Halifaxpublicgardens4b3d0c8a233060473e9c85eb17d3a2d2.jpg");
        Walk walk6 = new Walk("The Nerd Herd","18-Apr-2019 13:00:00","Killam Library - Dalhousie university","https://cdn.dal.ca/content/dam/dalhousie/images/campus-maps/killam.jpg.lt_3037db728a38513ccb92db303fa8b758.res/killam.jpg");
        Walk walk7 = new Walk("Jittery Coffee Junkies","19-Apr-2019 17:00:00", "Tim Hortons Spring Garden Road","http://rcsinc.ca/wp-content/uploads/2015/05/TimsSgRd-2.jpg");


        //adding test walks to the array list
        walkArrayList.add(walk1);
        walkArrayList.add(walk2);
        walkArrayList.add(walk3);
        walkArrayList.add(walk4);
        walkArrayList.add(walk5);
        walkArrayList.add(walk6);
        walkArrayList.add(walk7);

    }

}
