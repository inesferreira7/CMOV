package musicline.cmov.org.feup.musicline.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.speech.tts.TextToSpeech;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;
import musicline.cmov.org.feup.musicline.objects.Voucher;
import musicline.cmov.org.feup.musicline.utils.Globals;
import musicline.cmov.org.feup.musicline.objects.Ticket;

/**
 * Page of individual show. It shows some more detailed information and it is
 * where the user choose the number of tickets and purchased them
 */

public class ShowActivity extends AppCompatActivity {

    TextView title, date, price, total_price, description, place;
    ElegantNumberButton quantity_tickets;
    String actual_quantity;
    Button buy_tickets;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);

        Intent i = getIntent();
        final Show show = (Show)i.getSerializableExtra("Show");

        actual_quantity = "1"; //default number

        title = (TextView)findViewById(R.id.showpage_title);
        title.setText(show.getName());

        place = (TextView)findViewById(R.id.place_info);
        place.setText("   " + show.getPlace());

        date = (TextView)findViewById(R.id.date_info);
        date.setText("   " + show.getDate());

        price = (TextView)findViewById(R.id.price_info);
        price.setText("   " + show.getTicketPrice().toString() + "€");

        total_price = (TextView)findViewById(R.id.total_price_info);
        total_price.setText("   " + show.getTicketPrice().toString() + "€");

        description = (TextView)findViewById(R.id.show_description);
        description.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD); //justified description
        description.setText(show.getDescription());


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

                //Ensure customer confirmation
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowActivity.this);
                builder.setTitle("Purchase confirmation");
                builder.setMessage("Are you sure you want to purchase " + actual_quantity + " tickets?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        buyTickets(show);
                        for(int q=1; q <= Integer.parseInt(actual_quantity); q++) {
                            createVoucher(false);
                        }
                        if(Integer.parseInt(actual_quantity) * show.getTicketPrice().intValue() % 100 == 0){
                            createVoucher(true);
                        }

                        Toast.makeText(ShowActivity.this, "Purchased ticket successfully!", Toast.LENGTH_SHORT).show();
                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();

            }
        });
    }

    /**
     * Creates voucher. For each bought ticket, it is created a cafeteria voucher with free
     * coffee or free popcorn randomly selected. If the total paid value is a multiple of
     * 100€, it is generated another kind of voucher that gives 5% discount in a cafeteria
     * order.
     * @param discount
     */

    public void createVoucher(boolean discount) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Globals.URL + "/voucher";
        final SharedPreferences prefs = this.getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);

        JSONObject body = new JSONObject();
        Random rand = new Random();
        int type = rand.nextInt(2) + 0;
        String type_string = new String();

        if(discount) type = 2;

        if (type == 0) {
            type_string = "Coffee";
        } else if(type == 1){
            type_string = "Popcorn";
        } else if(type == 2){
            type_string = "5%";
        }

        try {
            body.put("customerId", prefs.getString("uuid", ""));
            body.put("type", type_string);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject voucher) {
                        String json = prefs.getString("vouchers", null);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Voucher>>() {}.getType();
                        List<Voucher> vouchers;

                        if (json != null){
                            vouchers = gson.fromJson(json, type);
                        } else {
                            vouchers = new ArrayList<>();
                        }

                        try {
                            Voucher v = new Voucher(
                                    voucher.getString("_id"),
                                    voucher.getString("customerId"),
                                    voucher.getString("type"),
                                    voucher.getBoolean("isUsed")
                            );

                            vouchers.add(v);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        SharedPreferences.Editor editor = getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE).edit();
                        json = gson.toJson(vouchers);
                        editor.putString("vouchers", json);
                        editor.apply();
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

    /**
     * Buy tickets. Associates the number of purchased tickets with the customer. Then
     * he can consult the list of his tickets and validate them
     * @param show
     */

    public void buyTickets(Show show) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Globals.URL + "/tickets/buy";
        final SharedPreferences prefs = this.getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);

        JSONObject body = new JSONObject();
        Random rand = new Random();

        //random number (no need to care about availability and ensuring unique places)
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
                        String json = prefs.getString("tickets", null);
                        Gson gson = new Gson();
                        Type type = new TypeToken<ArrayList<Ticket>>() {}.getType();
                        List<Ticket> ticket_list;

                        if (json != null){
                            ticket_list = gson.fromJson(json, type);
                        } else {
                            ticket_list = new ArrayList<>();
                        }

                        for(int i = 0; i < tickets.length(); i++){
                            try {
                                JSONObject t = (JSONObject) tickets.get(i);

                                Ticket ticket = new Ticket(
                                        t.getString("_id"),
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
                        gson = new Gson();
                        json = gson.toJson(ticket_list);
                        editor.putString("tickets", json);
                        editor.apply();
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
