package musicline.cmov.org.feup.musicline.fragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;
import musicline.cmov.org.feup.musicline.utils.Globals;

import static android.content.Context.MODE_PRIVATE;

public class MyTicketsFragment extends Fragment {

    SharedPreferences prefs;
    JSONArray tickets_json;

    public MyTicketsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shows, container, false);

        prefs = view.getContext().getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        tickets_json = new JSONArray();

        listTickets(tickets_json);

        return view;
    }

    public void listTickets(final JSONArray ticketList) {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = Globals.URL + "/tickets/customer/" + prefs.getString("uuid", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, ticketList, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                Log.i("Response", response.toString());
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error API call", error.toString());
                    }
                });

        queue.add(request);
    }

}
