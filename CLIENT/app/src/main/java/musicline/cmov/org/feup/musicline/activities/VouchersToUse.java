package musicline.cmov.org.feup.musicline.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.adapters.VouchersToUseAdapter;
import musicline.cmov.org.feup.musicline.fragments.CafeteriaFragment;
import musicline.cmov.org.feup.musicline.fragments.MyVouchersFragment;
import musicline.cmov.org.feup.musicline.objects.Ticket;
import musicline.cmov.org.feup.musicline.objects.Voucher;
import musicline.cmov.org.feup.musicline.utils.Globals;

/**
 * Handles adding vouchers to cafeteria orders. When placing an order, a customer can
 * use at most two of his unused vouchers (only one 5% voucher is accepted in each order).
 */
public class VouchersToUse extends AppCompatActivity {

    SharedPreferences prefs;
    JSONArray vouchers_json;
    ArrayList<Voucher> vouchers = new ArrayList<>();
    ListView list_vouchers;
    VouchersToUseAdapter<Voucher> voucher_adapter;
    SparseBooleanArray spa;
    HashMap<String, Integer> orderProducts;
    Button addVouchers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vouchers_to_use);

        Intent intent = getIntent();
        orderProducts = (HashMap<String, Integer>) intent.getSerializableExtra("Order");

        addVouchers = (Button)findViewById(R.id.addVouchers);

        spa = new SparseBooleanArray();

        prefs = getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        vouchers_json = new JSONArray();
        ConnectivityManager connectivityManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if( activeNetworkInfo != null && activeNetworkInfo.isConnected() ){
            listVouchers(vouchers_json);
        } else {
            String json = prefs.getString("vouchers", null);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Ticket>>() {}.getType();

            if (json != null){
                vouchers = gson.fromJson(json, type);
            }
        }
        list_vouchers = (ListView)findViewById(R.id.list_vouchers_to_use);
        voucher_adapter = new VouchersToUseAdapter(getBaseContext(), vouchers, orderProducts);


        addVouchers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Voucher> vouchersToUse = voucher_adapter.getCheckedItems();

                Intent i = new Intent();
                i.putExtra("vouchers", vouchersToUse);
                setResult(RESULT_OK, i);
                finish();
            }
        });
    }

    public void listVouchers(final JSONArray voucherList) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Globals.URL + "/vouchers/customer/" + prefs.getString("uuid", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, voucherList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject voucher = (JSONObject) response.get(i);

                                Voucher v = new Voucher(
                                        voucher.getString("_id"),
                                        voucher.getString("customerId"),
                                        voucher.getString("type"),
                                        voucher.getBoolean("isUsed")
                                );

                                if(!voucher.getBoolean("isUsed")) //only unused vouchers
                                    vouchers.add(v);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SharedPreferences.Editor editor = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(vouchers);
                        editor.putString("vouchers", json);
                        editor.apply();

                        onSuccess(vouchers);
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

    private void onSuccess(List<Voucher> vouchers) {
        list_vouchers.setAdapter(voucher_adapter);
    }

}
