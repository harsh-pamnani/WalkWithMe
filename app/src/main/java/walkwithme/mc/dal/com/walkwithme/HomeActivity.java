package walkwithme.mc.dal.com.walkwithme;

import android.app.Activity;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.view.View;

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

        //get current GPS location

        //if location is given

            //get list of walks

            //for each walk get coordinate and compare to current location

                //sort list by nearest walks


        //else if location is not giver

            //random order or by date



        //Create a ListOnClickListener for walkList


        //Create click listener for add button
        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //change event

            }
        });


    }

}
