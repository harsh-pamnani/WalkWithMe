package walkwithme.mc.dal.com.walkwithme;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actvity_view_temp);

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
    }



}
