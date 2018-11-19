package musicline.cmov.org.feup.musicline.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ScaleDrawable;
import android.media.Image;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import musicline.cmov.org.feup.musicline.activities.ShowActivity;
import musicline.cmov.org.feup.musicline.activities.VouchersToUse;
import musicline.cmov.org.feup.musicline.adapters.MyAdapter;
import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;
import musicline.cmov.org.feup.musicline.utils.Globals;

import static android.content.Context.MODE_PRIVATE;

public class CafeteriaFragment extends Fragment{

    HashMap<Globals.Item, Integer> order;
    CardView coffee_card, soda_card, popcorn_card, sandwich_card;
    ListView order_list;
    MyAdapter order_adapter;
    double order_total;
    TextView order_total_text;
    Button create_order, add_vouchers;
    ImageButton delete_order;

    public CafeteriaFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        delete_order = (ImageButton)view.findViewById(R.id.clear_button);
        delete_order.setVisibility(View.INVISIBLE);

        order = new HashMap<>();
        order_list = (ListView) view.findViewById(R.id.order_list);
        order_adapter = new MyAdapter(order);
        order_list.setAdapter(order_adapter);

        order_total = 0;
        order_total_text= (TextView)view.findViewById(R.id.order_total);
        order_total_text.setTextColor(getResources().getColor(R.color.colorPrimary));
        order_total_text.setTypeface(Typeface.DEFAULT_BOLD);
        
        add_vouchers = (Button)view.findViewById(R.id.addVouchers_button);
        add_vouchers.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), VouchersToUse.class);
                startActivity(intent);
            }
        });

        delete_order.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View view) {
                order.clear();
                order_adapter.updateList(order);
                order_total=0;
                order_total_text.setVisibility(View.INVISIBLE);
                delete_order.setVisibility(View.INVISIBLE);
            }
        } );


        coffee_card = (CardView) view.findViewById(R.id.coffee_card);
        coffee_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Key existence check
                Integer quantity = order.get(Globals.Item.Coffee);
                if(quantity == null) {
                    delete_order.setVisibility(View.VISIBLE);
                    order.put(Globals.Item.Coffee, 1);
                    order_total+= Globals.COFFEE_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                    order_total_text.setVisibility(View.VISIBLE);
                }
                else {
                    order.put(Globals.Item.Coffee, quantity+1); //override
                    order_total+= Globals.COFFEE_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                }

                order_adapter.updateList(order);
            }
            });

        soda_card = (CardView) view.findViewById(R.id.soda_card);
        soda_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.Soda);
                if(quantity == null) {
                    delete_order.setVisibility(View.VISIBLE);
                    order.put(Globals.Item.Soda, 1);
                    order_total+= Globals.SODA_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                    order_total_text.setVisibility(View.VISIBLE);
                }
                else {
                    order.put(Globals.Item.Soda, quantity+1); //override
                    order_total+= Globals.SODA_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                }
                order_adapter.updateList(order);
            }
        });

        popcorn_card = (CardView) view.findViewById(R.id.popcorn_card);
        popcorn_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.Popcorn);
                if(quantity == null) {
                    delete_order.setVisibility(View.VISIBLE);
                    order.put(Globals.Item.Popcorn, 1);
                    order_total+= Globals.POPCORN_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                    order_total_text.setVisibility(View.VISIBLE);
                }
                else {
                    order.put(Globals.Item.Popcorn, quantity+1); //override
                    order_total+= Globals.POPCORN_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                }

                order_adapter.updateList(order);
            }
        });

        sandwich_card = (CardView) view.findViewById(R.id.sandwich_card);
        sandwich_card.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.Sandwich);
                if(quantity == null) {
                    delete_order.setVisibility(View.VISIBLE);
                    order.put(Globals.Item.Sandwich, 1);
                    order_total+= Globals.SANDWICH_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                    order_total_text.setVisibility(View.VISIBLE);
                }
                else {
                    order.put(Globals.Item.Sandwich, quantity+1); //override
                    order_total+= Globals.SANDWICH_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                }

                order_adapter.updateList(order);
            }
        });

        create_order = (Button) view.findViewById(R.id.order_button);
        create_order.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(order.isEmpty()){
                    Toast.makeText(getActivity(), "Empty order. Please select at least one product", Toast.LENGTH_SHORT).show();
                }
                else {
                    newOrder();
                    Toast.makeText(getActivity(), "Placed order successfully!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    public void newOrder(){
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = Globals.URL + "/order";
        SharedPreferences prefs = this.getContext().getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);

        JSONObject body = new JSONObject();

        try {
            JSONArray products = getOrderProducts();

            body.put("customerId", prefs.getString("uuid", ""));
            body.put("products", products);
            body.put("vouchers", new JSONArray());
            body.put("totalPrice", order_total);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, body,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.i("Order response", response.toString());
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

    public JSONArray getOrderProducts(){
        JSONArray products = new JSONArray();

        for(Map.Entry<Globals.Item, Integer> entry : this.order.entrySet()) {
            Globals.Item item = entry.getKey();
            int quantity = entry.getValue();

            JSONObject order_item = new JSONObject();
            try {
                order_item.put("product", item);
                order_item.put("quantity", quantity);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            products.put(order_item);
        }

        return products;
    }

}
