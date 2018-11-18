package musicline.cmov.org.feup.musicline.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Order;
import musicline.cmov.org.feup.musicline.utils.Globals;

import static android.content.Context.MODE_PRIVATE;

public class MyOrdersFragment extends Fragment {
    List<Order> orders = new ArrayList<>();
    ListView list_all_orders;
    ArrayAdapter<Order> all_orders_adapter;

    SharedPreferences prefs;
    JSONArray orders_json;
    private ProgressBar spinner;

    LinearLayout linearLayout;

    public MyOrdersFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_orders, container, false);

        prefs = view.getContext().getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        orders_json = new JSONArray();

        spinner = (ProgressBar)view.findViewById(R.id.progress_tickets);

        orders = new ArrayList<>();

        listOrders(orders_json);
        list_all_orders = (ListView)view.findViewById(R.id.list_all_orders);
        all_orders_adapter = new OrderAdapter();

        return view;
    }

    public void listOrders(final JSONArray orderList) {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = Globals.URL + "/orders/customer/" + prefs.getString("uuid", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, orderList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject order = (JSONObject) response.get(i);

                                JSONArray products = order.getJSONArray("products");
                                JSONArray vouchers = order.getJSONArray("vouchers");

                                HashMap<String, Integer> prods = new HashMap<>();
                                ArrayList<String> vouchs = new ArrayList<>();

                                for(int p = 0; p < products.length(); p++){
                                    JSONObject product = products.getJSONObject(p);

                                    prods.put(product.getString("product"), product.getInt("quantity"));
                                }

                                for(int v = 0; v < vouchers.length(); v++){
                                    JSONObject voucher = vouchers.getJSONObject(v);

                                    vouchs.add(voucher.getString("voucherId"));
                                }

                                Order o = new Order(
                                    order.getString("_id"),
                                    order.getString("customerId"),
                                    prods,
                                    vouchs,
                                    order.getDouble("totalPrice"),
                                    order.getBoolean("validated")
                                );
                                
                                orders.add(o);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        onSuccess(orders);
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

    private void onSuccess(List<Order> orders) {
        list_all_orders.setAdapter(all_orders_adapter);
    }

    private class OrderAdapter extends ArrayAdapter<Order> {

        OrderAdapter() {
            super(MyOrdersFragment.this.getContext(), R.layout.all_orders_adapter, orders);
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.all_orders_adapter, parent, false);
            }

            Order o = orders.get(position);
            HashMap<String, Integer> products = o.getProducts();
            Log.e("products", products.toString());
            linearLayout = ((LinearLayout) row.findViewById(R.id.all_products));

            for (Map.Entry<String, Integer> entry : products.entrySet()) {
                LinearLayout layout = new LinearLayout(getContext());
                layout.setOrientation(LinearLayout.HORIZONTAL);
                TextView product = new TextView(getContext());
                product.setText(entry.getKey());
                layout.addView(product);
                TextView quantity = new TextView(getContext());
                quantity.setText("  " + Integer.toString(entry.getValue()) +"x");
                layout.addView(quantity);
                linearLayout.addView(layout);
            }

            return row;
        }
    }
}