package musicline.cmov.org.feup.musicline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.List;

import musicline.cmov.org.feup.musicline.utils.Show;

public class ShowsFragment extends Fragment {

    List<Show> shows = new ArrayList<>();
    ArrayAdapter<Show> show_adapter;
    ListView list_shows;

    public ShowsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_shows, container, false);

        listShows(shows);
        Log.e("shows", shows.toString());

        list_shows = (ListView)view.findViewById(R.id.list_shows);
        show_adapter = new ShowAdapter();

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
                        Log.e("SHOOOOWS", shows.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                Log.i("SHOWS", shows.toString());
                onSuccess(shows);
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

    private void onSuccess(List<Show> shows){

        list_shows.setAdapter(show_adapter);

    }

    private class ShowAdapter extends ArrayAdapter<Show> {

        ShowAdapter(){super(ShowsFragment.this.getContext(), R.layout.show_adapter, shows);}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View row = convertView;
            if (row == null) {
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.show_adapter, parent, false);
            }

            Show s = shows.get(position);
            ((TextView)row.findViewById(R.id.show_title)).setText(s.getName());
            ((TextView)row.findViewById(R.id.show_day)).setText("Dia");
            ((TextView)row.findViewById(R.id.show_hour)).setText("Hora");
            ((TextView)row.findViewById(R.id.show_price)).setText(String.valueOf(s.getTicketPrice()));
            ImageView icon = row.findViewById(R.id.show_icon);
            icon.setImageResource(R.drawable.show_icon);
            return (row);
        }
    }
}
