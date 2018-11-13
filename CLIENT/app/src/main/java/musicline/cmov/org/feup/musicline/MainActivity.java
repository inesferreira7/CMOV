package musicline.cmov.org.feup.musicline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_cafeteria);

        SharedPreferences prefs = getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        String uuid = prefs.getString("uuid", null);
        if (uuid != null) {
            Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
            startActivity(intent);
            finish();
        } else {
            //Register
            Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
