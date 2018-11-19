package com.example.nuno.terminal;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";
    Button ticketButton;
    Button orderButton;
    public static String URL = "https://36d51b19.ngrok.io";
    List<Ticket> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tickets = new ArrayList<>();

        ticketButton = (Button) findViewById(R.id.scanButton);
        orderButton = (Button) findViewById(R.id.orderButton);

        ticketButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanTickets(true);
            }
        });

        orderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanOrders(true);
            }
        });
    }

    public void scanTickets(boolean qrcode) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", qrcode ? "QR_CODE_MODE" : "PRODUCT_MODE");
            startActivityForResult(intent, 0);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void scanOrders(boolean qrcode) {
        try {
            Intent intent = new Intent(ACTION_SCAN);
            intent.putExtra("SCAN_MODE", qrcode ? "QR_CODE_MODE" : "PRODUCT_MODE");
            startActivityForResult(intent, 1);
        }
        catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                JSONObject tickets = new JSONObject();

                try {
                    tickets = new JSONObject(data.getStringExtra("SCAN_RESULT"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                validateTickets(tickets);
            }
        } else if (requestCode == 1){
            if (resultCode == RESULT_OK){
                JSONObject order = new JSONObject();

                try{
                    order = new JSONObject(data.getStringExtra("SCAN_RESULT"));
                    Log.i("order", order.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                validateOrder(order);
            }
        }
    }

    public void validateTickets(final JSONObject ts){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URL + "/tickets/validate";

        JSONArray array = new JSONArray();
        array.put(ts);

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.POST, url, array,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).create();
                        dialog.setTitle("Ticket validation");
                        dialog.setMessage("All tickets were validated");
                        dialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        dialog.show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error API call", error.toString());
                    }
                }
        );

        queue.add(request);
    }

    public void validateOrder(final JSONObject o){
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = URL + "/order/validate";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, o,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            JSONArray vouchers = response.getJSONArray("vouchers");
                            boolean isValidated = response.getBoolean("isValidated");
                            int orderNumber = response.getInt("orderNumber");
                            JSONArray products = response.getJSONArray("products");
                            double totalPrice = response.getDouble("totalPrice");
                            String nif = response.getString("nif");
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        Log.i("Response", response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error API call", error.toString());
                    }
                }
        );

        queue.add(request);
    }
}
