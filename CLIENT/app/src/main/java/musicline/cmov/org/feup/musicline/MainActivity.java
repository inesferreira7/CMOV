package musicline.cmov.org.feup.musicline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Register
        Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
        startActivity(intent);
        finish();
    }
}
