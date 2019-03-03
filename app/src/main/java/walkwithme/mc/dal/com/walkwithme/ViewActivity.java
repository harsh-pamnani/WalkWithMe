package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONException;
import org.json.JSONObject;

import walkwithme.mc.dal.com.walkwithme.ActivityJsonObj.ViewActivityJsonObj;

public class ViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Map Variable
    private GoogleMap mMap;

    //Initializing JSON Object
    JSONObject event = null;

    //Initializing views
    CarouselView carouselView;
    TextView location;
    TextView date;
    TextView description;
    TextView eventName;

    //Fetching Images from the Drawable directory
    int[] sampleImages = {R.drawable.walk_1, R.drawable.walk_2, R.drawable.walk_3, R.drawable.walk_4};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Code Snippet From GOOGLE MAP API Doc
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Intent object passed from Home Activity
        Intent intent = getIntent();

        //Creating viewActivityJsonObj class object to parse the string to JSON Object
        ViewActivityJsonObj viewActivityJsonObj = new ViewActivityJsonObj();

        //Fetching the resources passed in the  Intent from Home Activity
        event = viewActivityJsonObj.saveData(intent.getStringExtra("event"));

        //Fetching required data from JSON Object
        location = (TextView) findViewById(R.id.location);
        date = (TextView) findViewById(R.id.date);
        description = (TextView) findViewById(R.id.description);
        eventName = (TextView) findViewById(R.id.eventName);

        try {
            // Setting the JSON data in the Activity
            location.setText(event.get("location").toString());
            date.setText(event.get("dateTime").toString());
            description.setText(event.get("description").toString());
            eventName.setText(event.get("eventName").toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }


        //Creating the carousel View for the event images
        carouselView = (CarouselView) findViewById(R.id.carousel_view);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);
    }

    // Code to swipe the images in the carousel
    ImageListener imageListener = new ImageListener() {
        @Override
        public void setImageForPosition(int position, ImageView imageView) {
            imageView.setImageResource(sampleImages[position]);
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        // Add a marker at the Event Location
        LatLng eventLoc = null;
        try {
            // Setting the location coordinates in google Map
            eventLoc = new LatLng(Double.valueOf(event.get("coordinateLang").toString()), Double.valueOf(event.get("coordinateLat").toString()));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        // Adding Marker text in Google Map
        mMap.addMarker(new MarkerOptions().position(eventLoc).title("Your Meeting Location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLoc));

    }

}
