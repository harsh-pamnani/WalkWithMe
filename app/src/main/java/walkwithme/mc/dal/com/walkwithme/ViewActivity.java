package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
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

public class ViewActivity extends AppCompatActivity implements OnMapReadyCallback {

    //Map Variable
    private GoogleMap mMap;

    Double eventCoordinateLang = 44.637386;
    Double eventCoordinateLong = -63.587347;

    LatLng eventLoc = null;

    //Initializing views
    CarouselView carouselView;
    TextView location;
    TextView date;
    TextView description;
    TextView eventNameView;

    //Fetching Images from the Drawable directory
    int[] sampleImages = {R.drawable.walk_2, R.drawable.walk_1, R.drawable.walk_3, R.drawable.walk_4};
    private String[] imageUrls = new String[]{
            "https://cdn.pixabay.com/photo/2016/11/11/23/34/cat-1817970_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/21/12/26/glowworm-3031704_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/12/24/09/09/road-3036620_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/11/07/00/07/fantasy-2925250_960_720.jpg",
            "https://cdn.pixabay.com/photo/2017/10/10/15/28/butterfly-2837589_960_720.jpg"
    };

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
        // Setting the JSON data in the Activity
        location.setText(eventLocation);
        date.setText(eventDatetime);
        description.setText(eventDescription);
        eventNameView.setText(eventName);

        //Creating the carousel View for the event images
//        carouselView = (CarouselView) findViewById(R.id.carousel_view);
//        carouselView.setPageCount(sampleImages.length);
//        carouselView.setImageListener(imageListener);
        ViewPager viewPager = findViewById(R.id.view_pager_Image);
        PagerViewAdapter adapter = new PagerViewAdapter(this, imageUrls);
        viewPager.setAdapter(adapter);

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

