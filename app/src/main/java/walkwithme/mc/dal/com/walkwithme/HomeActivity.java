package walkwithme.mc.dal.com.walkwithme;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.icu.text.SymbolTable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.View;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

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

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class HomeActivity extends AppCompatActivity {

    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;
    private static final String TAG = "MainActivity";

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mLocationPermissionGranted = false;
    private boolean firstOnStart = true;
    private boolean gpsCoordinatesFound = false;

    //default location is Citadel Hill, Halifax, NS
    Double currentLatitude = 44.647398;
    Double currentLongitude  = -63.580364;

    ListView walkList;
    FloatingActionButton addFab;
    ArrayList<Walk> walkArrayList = new ArrayList<>();





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // Code will execute if there is an internet connectivity
        if (isNetworkStatusAvialable(getApplicationContext())) {
            Log.d(TAG, "onCreate: entered");

            //instantiate the location provider client
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            //get the location permissions, if they do not exists prompt them
            getLocationPermission();


            // Get walk data from the firebase database
            final FirebaseDatabase database = FirebaseDatabase.getInstance();
            DatabaseReference ref = database.getReference("WALK_DATA");

            // Listener to monitor the changes in the firebase node.
            // If new event exists, all walks are returned again.
            ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Log.d(TAG, "onDataChange: Data located through event listener");

                    //Refresh the walkArray list to ensure no duplicates
                    walkArrayList = new ArrayList<>();

                    Iterator<DataSnapshot> it = dataSnapshot.getChildren().iterator();
                    while (it.hasNext()) {
                        CreateActivityForm walkDat = it.next().getValue(CreateActivityForm.class);

                        //add each walk to the walkArrayList
                        walkArrayList.add(new Walk(currentLatitude, currentLongitude, walkDat.latitude, walkDat.longitude, walkDat.id, walkDat.title, walkDat.date, walkDat.time, walkDat.location, walkDat.imageURL.get(0), walkDat.imageURL, walkDat.description));

                    }

                    Log.d(TAG, "onDataChange: [" + currentLatitude + "," + currentLongitude + "]");

                    //if the GPS coordinates have been found, sort, otherwise display as-is
                    if (gpsCoordinatesFound) {
                        displayList(true);
                    } else {
                        displayList(false);
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    System.out.println("The read failed: " + databaseError.getCode());
                    Toast.makeText(getApplicationContext(), "Oops! Error Occured while fetchig data.", Toast.LENGTH_LONG).show();
                }
            });


            walkList = findViewById(R.id.list_walks);
            addFab = findViewById(R.id.addFab);

            //OnClickListener if one of the list items is clicked
            walkList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int i, long id) {

                    //create intent and bundle for View Activity
                    Intent viewIntent = new Intent(HomeActivity.this, ViewActivity.class);
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
                    viewBundle.putStringArrayList("imageLoaderURL", walkArrayList.get(i).getCarouselImages());

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
                    Intent createIntent = new Intent(HomeActivity.this, CreateActivity.class);
                    startActivity(createIntent);
                }
            });

        //Toast will populate saying that internet is not available
        }else {
            Toast.makeText(getApplicationContext(), "Internet is not available", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: entered (GPS)");

        //if on first onStart, wait for permission request to make decision
        if(firstOnStart){
            firstOnStart = false;


            //else on the next onStarts,
        }else {

            //if Location permission has been granted, go get the coordinates
            if (mLocationPermissionGranted) {
                Log.d(TAG, "onStart: permission granted");
                getLastLocation();

                //else display the list without getting location
            } else {
                Log.d(TAG, "onStart: permission false");
                displayList(false);
            }

        }
    }

    //Method to check internet connectivity
    public static boolean isNetworkStatusAvialable(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivityManager != null) {
            NetworkInfo netInfos = connectivityManager.getActiveNetworkInfo();
            if (netInfos != null)
                if (netInfos.isConnected())
                    return true;
        }
        return false;
    }

    /**
     * Checks if Location permission has been granted in the past
     * https://developer.android.com/training/permissions/requesting.html
     * https://developer.android.com/guide/topics/permissions/overview
     */
    private void getLocationPermission(){

        // If permission is not granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            //request the permission
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);

            // PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION is an
            // app-defined int constant. The callback method gets the
            // result of the request.

            // Result determined in onRequestPermissionResult

            //else permission is granted (has been granted in the past)
        }else{

            mLocationPermissionGranted = true;

            getLastLocation();
        }

    }

    /**
     * https://developer.android.com/training/permissions/requesting.html
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {

                // If request is cancelled/denied, the result arrays are empty.
                // If request has been granted
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    mLocationPermissionGranted = true;
                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    Log.d(TAG, "onRequestPermissionsResult: Granted!");

                    getLastLocation();

                    //else if the request has been denied
                }else{

                    mLocationPermissionGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: Denied!");

                    displayList(false);
                }
            }
        }
    }


    private void displayList(boolean gpsReceived){

        Log.d(TAG, "displayList: entered gps: " + gpsReceived);
//
//        //array containing all walk items
//        walkArrayList = new ArrayList<>();
//
//        //populate array
//        retrieveData();

        //if the gps is available sort by distance to user
        if(gpsReceived) {
            sortWalksByDistance();
        }

        //create Adapter instance
        WalkListAdapter walkListAdapter = new WalkListAdapter(this, R.layout.listview_item, walkArrayList);

        //apply the adapter to the walk ArrayList
        walkList.setAdapter(walkListAdapter);
    }


    /**
     * Method that sorts the walkArrayList based on distance to user calculated value.
     * Value is calculated in the Walk class constructor given user and location coordinates.
     */
    private void sortWalksByDistance(){

        Collections.sort(walkArrayList, new Comparator<Walk>() {
            @Override
            public int compare(Walk walk1, Walk walk2) {

                return walk1.getDistanceToUser().compareTo(walk2.getDistanceToUser());
            }
        });

    }



    /**
     * https://developer.android.com/training/location/retrieve-current.html
     */
    private void getLastLocation() {

        Log.d(TAG, "getLastLocation: entered");
        //mandatory permission check required before getting last location
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            displayList(false);
            return;
        }

        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                // Got last known location. In some rare situations this can be null.
                if (location != null) {
                    Log.d(TAG, "onSuccess: location not null");

                    currentLatitude = location.getLatitude();
                    currentLongitude = location.getLongitude();
                    Log.d(TAG, "onSuccess: ["+ currentLatitude +","+ currentLongitude +"]");

                    gpsCoordinatesFound = true;

                    displayList(true);

                }else{
                    Log.d(TAG, "onSuccess: location null");
                    displayList(false);
                }
            }
        });
    }


}


