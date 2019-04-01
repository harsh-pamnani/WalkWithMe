package walkwithme.mc.dal.com.walkwithme;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    Button skipForNow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        skipForNow = (Button) findViewById(R.id.button_skip_for_now);

        skipForNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent createIntent= new Intent(LoginActivity.this, CreateActivity.class);
                startActivity(createIntent);
            }
        });
    }
}
