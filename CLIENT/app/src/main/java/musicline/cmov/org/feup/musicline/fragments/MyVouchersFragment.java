package musicline.cmov.org.feup.musicline.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Ticket;
import musicline.cmov.org.feup.musicline.objects.Voucher;
import musicline.cmov.org.feup.musicline.utils.Globals;

import static android.content.Context.MODE_PRIVATE;

public class MyVouchersFragment extends Fragment {

    SharedPreferences prefs;
    JSONArray vouchers_json;
    List<Voucher> vouchers = new ArrayList<>();
    ListView list_vouchers;
    ArrayAdapter<Voucher> voucher_adapter;

    public MyVouchersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_vouchers, container, false);
        prefs = view.getContext().getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        vouchers_json = new JSONArray();


        ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
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

        list_vouchers = (ListView)view.findViewById(R.id.list_vouchers);
        voucher_adapter = new VoucherAdapter();

        return view;
    }

    public void listVouchers(final JSONArray voucherList) {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
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

    private void onSuccess(List<Voucher> tickets) {
        //spinner.setVisibility(View.GONE);
        list_vouchers.setAdapter(voucher_adapter);
    }

    private class VoucherAdapter extends ArrayAdapter<Voucher> {

        VoucherAdapter(){super(MyVouchersFragment.this.getContext(), R.layout.voucher_adapter, vouchers);}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            View row = convertView;
            if(row == null){
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.voucher_adapter, parent, false);
            }

            Voucher v = vouchers.get(position);
            ((TextView)row.findViewById(R.id.customer_id)).setText(v.getCustomerId());
            return (row);
        }
    }
}
