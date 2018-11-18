package musicline.cmov.org.feup.musicline.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import musicline.cmov.org.feup.musicline.objects.Ticket;

public class TicketActivity extends AppCompatActivity {

    Ticket ticket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        ticket = (Ticket) intent.getSerializableExtra("Ticket");

    }
}
