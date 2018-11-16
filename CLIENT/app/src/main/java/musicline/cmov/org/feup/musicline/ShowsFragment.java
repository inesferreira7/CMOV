package musicline.cmov.org.feup.musicline;


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
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import musicline.cmov.org.feup.musicline.utils.Show;

public class ShowsFragment extends Fragment {

    List<Show> shows = new ArrayList<>();

    public ShowsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shows, container, false);

        listShows(shows);

        return view;
    }

    private void listShows(final List<Show> shows) {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = Globals.URL + "/performances";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for(int i = 0; i < response.length(); i++){
                    try {
                        JSONObject show = (JSONObject) response.get(i);

                        Show s = new Show(
                                show.getString("_id"),
                                show.getString("name"),
                                show.getString("date"),
                                show.getInt("ticketPrice")
                        );

                        shows.add(s);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("SHOWS", shows.toString());
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
