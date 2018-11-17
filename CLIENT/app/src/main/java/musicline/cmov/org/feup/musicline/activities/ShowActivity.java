package musicline.cmov.org.feup.musicline.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;
import musicline.cmov.org.feup.musicline.utils.Globals;
import musicline.cmov.org.feup.musicline.utils.Ticket;

public class ShowActivity extends AppCompatActivity {

    TextView title, date, price, total_price;
    ElegantNumberButton quantity_tickets;
    String actual_quantity;
    Button buy_tickets;
    final List<Ticket> ticket_list = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent i = getIntent();
        final Show show = (Show)i.getSerializableExtra("Show");

        actual_quantity = "1";

        title = (TextView)findViewById(R.id.showpage_title);
        title.setText(show.getName());

        date = (TextView)findViewById(R.id.date_info);
        date.setText("   " + show.getDate());

        price = (TextView)findViewById(R.id.price_info);
        price.setText("   " + show.getTicketPrice().toString() + "€");

        total_price = (TextView)findViewById(R.id.total_price_info);
        total_price.setText("   " + show.getTicketPrice().toString() + "€");
        // total_price = quantity * price


        quantity_tickets = (ElegantNumberButton)findViewById(R.id.ticket_button);
        quantity_tickets.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                actual_quantity = quantity_tickets.getNumber();
                total_price.setText("   " + Integer.toString(Integer.parseInt(actual_quantity) * show.getTicketPrice().intValue()) + "€");
            }
        });

        buy_tickets = (Button)findViewById(R.id.buyTickets_button);
        buy_tickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("quantidade" , actual_quantity);
                buyTickets(show);
            }
        });
    }

    public void buyTickets(Show show) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Globals.URL + "/tickets/buy";
        SharedPreferences prefs = this.getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);

        JSONObject body = new JSONObject();
        Random rand = new Random();

        int  seat = rand.nextInt(500) + 1;

        try {
            body.put("performanceName", show.getName());
            body.put("performanceDate", show.getDate());
            body.put("performanceId", show.getId());
            body.put("customerId", prefs.getString("uuid", ""));
            body.put("seat", seat);
            body.put("quantity", actual_quantity);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray array = new JSONArray();

        array.put(body);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, array,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray tickets) {
                        Log.i("Ticket response", tickets.toString());

                        for(int i = 0; i < tickets.length(); i++){
                            try {
                                JSONObject t = (JSONObject) tickets.get(i);

                                Ticket ticket = new Ticket(
                                        t.getString("performanceName"),
                                        t.getString("performanceDate"),
                                        t.getString("performanceId"),
                                        t.getString("customerId"),
                                        t.getString("seat"),
                                        t.getBoolean("isUsed")
                                );

                                ticket_list.add(ticket);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SharedPreferences.Editor editor = getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE).edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(ticket_list);
                        editor.putString("tickets", json);
                        editor.apply();

                        //TODO Voltar para a lista de shows
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Volley error", error.toString());
                    }
                });

        queue.add(request);
    }
}
