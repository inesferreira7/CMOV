package musicline.cmov.org.feup.musicline;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import musicline.cmov.org.feup.musicline.utils.Show;

public class ShowActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent i = getIntent();
        Show show = (Show)i.getSerializableExtra("Show");
    }
}
