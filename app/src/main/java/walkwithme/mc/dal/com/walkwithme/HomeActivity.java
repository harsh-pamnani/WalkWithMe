package walkwithme.mc.dal.com.walkwithme;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

public class HomeActivity extends AppCompatActivity {



    public static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 9003;

    private static final String TAG = "MainActivity";

    private FusedLocationProviderClient mFusedLocationClient;
    private boolean mLocationPermissionGranted = false;

    Double currentLatitude = 44.647398;        //Citadel Hill as default
    Double currentLongitude  = -63.580364;

    ListView walkList;
    FloatingActionButton addFab;
    ArrayList<Walk> walkArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Log.d(TAG, "onCreate: entered");
        
        //instantiate the location provider client
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        getLocationPermission();

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
    protected void onStart() {
        super.onStart();

        Log.d(TAG, "onStart: entered (GPS)");

        //if Location permission has been granted, go get the coordinates
        if(mLocationPermissionGranted){
            Log.d(TAG, "onStart: permission granted");
            getLastLocation();

        //else display the list without getting location
        }else{
            Log.d(TAG, "onStart: permission false");
            displayList(false);
        }

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
        }

    }

    /**
     * https://developer.android.com/training/permissions/requesting.html
     * @param requestCode
     * @param permissions
     * @param grantResults
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

                //else if the request has been denied
                }else{

                     mLocationPermissionGranted = false;
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Log.d(TAG, "onRequestPermissionsResult: Denied!");
                }
            }
        }
    }


    private void displayList(boolean gpsReceived){

        Log.d(TAG, "displayList: entered gps: " + gpsReceived);

        //array containing all walk items
        walkArrayList = new ArrayList<>();

        //populate array
        retrieveData();

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
     * Method to populate the list of walks
     * Currently it is updated manually, future work requires it to be drawn from database
     */
    private void retrieveData() {


        Double currentLat  =  currentLatitude; //44.637386;
        Double currentLong = currentLongitude; //-63.587347;

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

                    displayList(true);

                }else{
                    Log.d(TAG, "onSuccess: location null");
                    displayList(false);
                }
            }
        });
    }


    /**
     * https://developer.android.com/training/location/receive-location-updates.html
     * https://developers.google.com/android/reference/com/google/android/gms/location/LocationRequest
     */


}
