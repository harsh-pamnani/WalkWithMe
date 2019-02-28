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


        Walk walk1 = new Walk("The Fats and the Furious","20-Apr-2019 2:00PM", "Citadel Hill Entrance");
        Walk walk2 = new Walk("The Mean Elder Machine","19-Apr-2019 5:00AM", "Elder Home #2");
        Walk walk3 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        /*
        Walk walk4 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        Walk walk5 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        Walk walk6 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        Walk walk7 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        Walk walk8 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        Walk walk9 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        Walk walk10 = new Walk("Mommy Group","19-Aug-2019 3:00PM", "Happy Day Care");
        */

        final ArrayList<Walk> walkArrayList = new ArrayList<>();

        walkArrayList.add(walk1);
        walkArrayList.add(walk2);
        walkArrayList.add(walk3);
        /*
        walkArrayList.add(walk4);
        walkArrayList.add(walk5);
        walkArrayList.add(walk6);
        walkArrayList.add(walk7);
        walkArrayList.add(walk8);
        walkArrayList.add(walk9);
        walkArrayList.add(walk10);
        */

        WalkListAdapter walkListAdapter = new WalkListAdapter(this, R.layout.listview_item, walkArrayList);
        walkList.setAdapter(walkListAdapter);


        /*
        final ArrayList<String> arrayList = new ArrayList<>();

        arrayList.add("Walk 1");
        arrayList.add("Walk 2");
        arrayList.add("Walk 3");
        arrayList.add("Walk 4");
        arrayList.add("Walk 5");
        arrayList.add("Walk 6");
        arrayList.add("Walk 7");
        arrayList.add("Walk 8");
        arrayList.add("Walk 9");
        arrayList.add("Walk 10");
        arrayList.add("Walk 11");
        arrayList.add("Walk 12");
        arrayList.add("Walk 13");

        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, arrayList);

        walkList.setAdapter(arrayAdapter);
        */



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
