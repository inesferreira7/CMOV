package musicline.cmov.org.feup.musicline.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;

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
import musicline.cmov.org.feup.musicline.activities.ValidateTicketsActivity;
import musicline.cmov.org.feup.musicline.adapters.TicketAdapter;
import musicline.cmov.org.feup.musicline.objects.Ticket;
import musicline.cmov.org.feup.musicline.utils.Globals;

import static android.content.Context.MODE_PRIVATE;

/**
 *  Lists all tickets of a user. A user can choose at most 4 tickets to validate in terminal
 */

public class MyTicketsFragment extends Fragment {

    ArrayList<Ticket> tickets;
    TicketAdapter<Ticket> ticket_adapter;
    ListView list_tickets;

    SharedPreferences prefs;
    JSONArray tickets_json;
    private ProgressBar spinner;

    Button validate_button;

    //requires empty constructor
    public MyTicketsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        prefs = view.getContext().getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        tickets_json = new JSONArray();

        spinner = (ProgressBar)view.findViewById(R.id.progress_tickets);

        tickets = new ArrayList<>();

        ConnectivityManager connectivityManager = (ConnectivityManager) view.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if( activeNetworkInfo != null && activeNetworkInfo.isConnected() ){
            listTickets(tickets_json);
        } else {
            String json = prefs.getString("tickets", null);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Ticket>>() {}.getType();

            if (json != null){
                tickets = gson.fromJson(json, type);
            }
        }

        list_tickets = (ListView)view.findViewById(R.id.list_tickets);
        ticket_adapter = new TicketAdapter(view.getContext(), tickets);
        onSuccess(tickets);

        validate_button = (Button)view.findViewById(R.id.validate);
        validate_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Ticket> selected = new ArrayList<>();

                selected = ticket_adapter.getCheckedItems();

                Intent intent = new Intent(getContext(), ValidateTicketsActivity.class);
                intent.putExtra("Tickets", selected);
                startActivity(intent);
            }
        });

        return view;
    }

    public void listTickets(final JSONArray ticketList) {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = Globals.URL + "/tickets/customer/" + prefs.getString("uuid", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, ticketList,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try {
                                JSONObject ticket = (JSONObject) response.get(i);

                                Ticket t = new Ticket(
                                        ticket.getString("_id"),
                                        ticket.getString("performanceName"),
                                        ticket.getString("performanceDate"),
                                        ticket.getString("performanceId"),
                                        ticket.getString("customerId"),
                                        ticket.getString("seat"),
                                        ticket.getBoolean("isUsed")
                                );

                                tickets.add(t);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SharedPreferences.Editor editor = prefs.edit();
                        Gson gson = new Gson();
                        String json = gson.toJson(tickets);
                        editor.putString("tickets", json);
                        editor.apply();

                        onSuccess(tickets);
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

    private void onSuccess(List<Ticket> tickets) {
        spinner.setVisibility(View.GONE);
        list_tickets.setAdapter(ticket_adapter);
    }
}
