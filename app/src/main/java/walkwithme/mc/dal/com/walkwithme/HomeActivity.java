package walkwithme.mc.dal.com.walkwithme;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.view.View;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private Activity mActivty;

    ListView walkList;
    FloatingActionButton addFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mActivty = this;
        walkList = findViewById(R.id.list_walks);
        addFab = findViewById(R.id.addFab);


        //Sample walks for testing
        Walk walk1 = new Walk("The Fats and the Furious","20-Apr-2019 2:00PM", "Citadel Hill Entrance","https://hips.hearstapps.com/vader-prod.s3.amazonaws.com/1538172046-woman-walking-on-a-path-royalty-free-image-503818820-1538171480.jpg");
        Walk walk2 = new Walk("The Mean Elder Machine","19-Apr-2019 5:00AM", "Elder Home #2","");
        Walk walk3 = new Walk("Mommy Group","19-Apr-2019 3:00PM", "Happy Day Care","https://www.sbs.com.au/topics/sites/sbs.com.au.topics/files/styles/full/public/gettyimages-78616903.jpg?itok=vpRKMrOO&mtime=1502951087");



        //array containing all walk items
        final ArrayList<Walk> walkArrayList = new ArrayList<>();

        //adding test walks to the array list
        walkArrayList.add(walk1);
        walkArrayList.add(walk2);
        walkArrayList.add(walk3);

        walkArrayList.add(walk1);
        walkArrayList.add(walk2);
        walkArrayList.add(walk3);
        walkArrayList.add(walk1);
        walkArrayList.add(walk2);
        walkArrayList.add(walk3);



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


        //get current GPS location

        //if location is given

            //get list of walks

            //for each walk get coordinate and compare to current location

                //sort list by nearest walks


        //else if location is not giver

            //random order or by date


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

}
