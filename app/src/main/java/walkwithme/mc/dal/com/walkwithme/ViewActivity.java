package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.synnapps.carouselview.CarouselView;
import com.synnapps.carouselview.ImageListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import walkwithme.mc.dal.com.walkwithme.ActivityJsonObj.ViewActivityJsonObj;

public class ViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Map Variable
    private GoogleMap mMap;

    Double eventCoordinateLang = 44.637386;
    Double eventCoordinateLong = -63.587347;

    LatLng eventLoc = null;

    //Initializing views
    CarouselView carouselView;
    TextView location;
    TextView date, temperature;
    TextView description;
    TextView eventNameView;
    ImageView icon;

    // Variables for weather
    private static final String URL_START = "https://api.openweathermap.org/data/2.5/forecast?lat=";
    private static final String URL_MID = "&lon=";
    private static final String URL_END = "&appid=c7efb41660082306a3b0fe9ee27f770d";
    String latitude;
    String longitude;
    String weatherDate;

    //Fetching Images from the Drawable directory
    int[] sampleImages = {R.drawable.walk_2, R.drawable.walk_1, R.drawable.walk_3, R.drawable.walk_4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Code Snippet From GOOGLE MAP API Doc
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Intent object passed from Home Activity
        Intent intent = getIntent();

        //Fetching the resources passed in the  Intent from Home Activity
        //event = viewActivityJsonObj.saveData(intent.getStringExtra("event"));
        //pull data from the intent/bundle
        Bundle dataBundle = getIntent().getBundleExtra("bundle");

        //pull data from the bundle which allows for default values.
        final Integer eventId = dataBundle.getInt("eventID",-1);
        String eventName = dataBundle.getString("eventName", "N/A");
        String eventDatetime = dataBundle.getString("eventDatetime", "N/A");
        String eventLocation = dataBundle.getString("eventLocation", "N/A");
        String eventImageURL = dataBundle.getString("eventImageURL", "N/A");
        Double eventCoordinateLang = dataBundle.getDouble("eventCoordinateLang", 0.0);
        Double eventCoordinateLong = dataBundle.getDouble("eventCoordinateLong", 0.0);
        String eventDescription = dataBundle.getString("eventDescription", "N/A");
        String eventWeather = dataBundle.getString("eventWeather", "N/A");

        //Fetching required data from JSON Object
        location = (TextView) findViewById(R.id.location);
        date = (TextView) findViewById(R.id.date);
        description = (TextView) findViewById(R.id.description);
        eventNameView = (TextView) findViewById(R.id.eventNameView);
        temperature = (TextView) findViewById(R.id.temperature);
        icon = (ImageView) findViewById(R.id.icon);
        // Setting the JSON data in the Activity
        location.setText(eventLocation);
        date.setText(eventDatetime);
        description.setText(eventDescription);
        eventNameView.setText(eventName);

        //Creating the carousel View for the event images
        carouselView = (CarouselView) findViewById(R.id.carousel_view);
        carouselView.setPageCount(sampleImages.length);
        carouselView.setImageListener(imageListener);

        // Setting the location coordinates in google Map
        eventLoc = new LatLng(eventCoordinateLang, eventCoordinateLong);
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


        // Setting the location coordinates in google Map
        eventLoc = new LatLng(eventCoordinateLang, eventCoordinateLong);

        // Adding Marker text in Google Map
        mMap.addMarker(new MarkerOptions().position(eventLoc).title("Your Meeting LOcation"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(eventLoc));

    }
}

