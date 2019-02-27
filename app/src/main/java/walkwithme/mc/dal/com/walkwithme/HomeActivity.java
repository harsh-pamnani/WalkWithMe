package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import org.json.JSONObject;

import java.io.Serializable;

import walkwithme.mc.dal.com.walkwithme.ActivityJsonObj.ViewActivityJsonObj;

public class HomeActivity extends AppCompatActivity {
    // Sample View Avtivity JSON String
    String result = "{\n" +
            "    \"eventName\": \"The Fats and the Furious\",\n" +
            "    \"image\": [\n" +
            "      {\n" +
            "        \"image1\": \"image1\",\n" +
            "        \"image2\": \"image2\",\n" +
            "        \"image3\": \"image3\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"location\": \"Citadel,Halifax\",\n" +
            "    \"dateTime\": \"15-march-2019/8:30 AM\",\n" +
            "    \"coordinateLat\": \"-63.57\",\n" +
            "    \"coordinateLang\": \"44.64\",\n" +
            "    \"description\": \"Oh no! Hopefully, some of that fury goes into your walking game.\"\n" +
            "  }";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Button btn = (Button) findViewById(R.id.event);
        btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Intent creation for View Activity
                Intent detailsIntent= new Intent(HomeActivity.this,ViewActivity.class);
                detailsIntent.putExtra("event", result);
                startActivity(detailsIntent);
            }
        });
    }


}
