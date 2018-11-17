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
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;
import musicline.cmov.org.feup.musicline.objects.Ticket;
import musicline.cmov.org.feup.musicline.utils.Globals;

import static android.content.Context.MODE_PRIVATE;

public class MyTicketsFragment extends Fragment {

    List<Ticket> tickets = new ArrayList<>();
    ArrayAdapter<Ticket> ticket_adapter;
    ListView list_tickets;

    SharedPreferences prefs;
    JSONArray tickets_json;

    //requires empty constructor
    public MyTicketsFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_tickets, container, false);

        prefs = view.getContext().getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE);
        tickets_json = new JSONArray();

        listTickets(tickets_json);

        list_tickets = (ListView)view.findViewById(R.id.list_tickets);
        ticket_adapter = new TicketAdapter();

        return view;
    }

    public void listTickets(final JSONArray ticketList) {
        RequestQueue queue = Volley.newRequestQueue(this.getContext());
        String url = Globals.URL + "/tickets/customer/" + prefs.getString("uuid", "");

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, ticketList, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                onSuccess(tickets);
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

    private void onSuccess(List<Ticket> tickets) {
        list_tickets.setAdapter(ticket_adapter);
    }

    private class TicketAdapter extends ArrayAdapter<Ticket> {

        TicketAdapter(){super(MyTicketsFragment.this.getContext(), R.layout.ticket_adapter, tickets);}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View row = convertView;
            if(row == null){
                LayoutInflater inflater = getLayoutInflater();
                row = inflater.inflate(R.layout.ticket_adapter, parent,false);
            }

            Ticket t = tickets.get(position);
            ((TextView)row.findViewById(R.id.performance_name)).setText(t.getPerformanceName());
            return (row);
        }
    }
}
