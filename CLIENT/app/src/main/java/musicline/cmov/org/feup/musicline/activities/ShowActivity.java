package musicline.cmov.org.feup.musicline.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;

public class ShowActivity extends AppCompatActivity {

    TextView title, date, price;
    ElegantNumberButton quantity_tickets;
    String actual_quantity;
    Button buy_tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent i = getIntent();
        Show show = (Show)i.getSerializableExtra("Show");

        title = (TextView)findViewById(R.id.showpage_title);
        title.setText(show.getName());

        date = (TextView)findViewById(R.id.date_info);
        date.setText("   " + show.getDate());

        price = (TextView)findViewById(R.id.price_info);
        price.setText("   " + show.getTicketPrice().toString() + "€");

        quantity_tickets = (ElegantNumberButton)findViewById(R.id.ticket_button);
        quantity_tickets.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                actual_quantity = quantity_tickets.getNumber();
            }
        });

        buy_tickets = (Button)findViewById(R.id.buyTickets_button);
        buy_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("quantidade" , actual_quantity);
            }
        });



    }
}
