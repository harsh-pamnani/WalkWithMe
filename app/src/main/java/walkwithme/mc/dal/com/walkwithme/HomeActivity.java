package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import android.widget.Toast;

public class HomeActivity extends AppCompatActivity {

    ListView walkList;
    FloatingActionButton addFab;
    ArrayList<Walk> walkArrayList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Get walk data from the firebase database
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("WALK_DATA");

        // Listner to monitor the changes in the firebase node.
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> it =  dataSnapshot.getChildren().iterator();
                while(it.hasNext()){
                    CreateActivityForm walkDat = it.next().getValue(CreateActivityForm.class);
                    walkArrayList.add(new Walk(44.637386, -63.578754, 44.647824, -63.578754, walkDat.id, walkDat.title, walkDat.date, walkDat.location, walkDat.imageURL.get(0),walkDat.imageURL));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("The read failed: " + databaseError.getCode());
                Toast.makeText(getApplicationContext(),"Oops! Error Occured while fetchig data.", Toast.LENGTH_LONG).show();
            }
        });


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
                viewBundle.putString("eventId", walkArrayList.get(i).getEventId());
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


        //create Adapter instance
        WalkListAdapter walkListAdapter = new WalkListAdapter(this, R.layout.listview_item, walkArrayList);

        //apply the adapter to the walk ArrayList
        walkList.setAdapter(walkListAdapter);


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


