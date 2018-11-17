package musicline.cmov.org.feup.musicline.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.utils.Globals;

/**
 * Activity that launches the application. When a customer runs the application for
 * the first time he should register. Otherwise, he is presented with the homepage
 * and can then perform all the operations allowed by the app.
*/

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        SharedPreferences prefs = getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        String uuid = prefs.getString("uuid", null);
        if (uuid != null) { //meaning the user is already registered
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
