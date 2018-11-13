package musicline.cmov.org.feup.musicline;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CafeteriaFragment extends Fragment{

    HashMap<Globals.Item, Integer> order;
    CardView coffee_card, soda_card, popcorn_card, sandwich_card;
    public CafeteriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);
        order = new HashMap<>();

        coffee_card = (CardView) view.findViewById(R.id.coffee_card);
        coffee_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Key existence check
                Integer quantity = order.get(Globals.Item.COFFEE);
                if(quantity == null) {
                    order.put(Globals.Item.COFFEE, 1);
                    Log.e("oi", order.toString());
                }
                else {
                    order.put(Globals.Item.COFFEE, quantity+1); //override
                    Log.e("new", order.toString());
                }
            }
        });

        soda_card = (CardView) view.findViewById(R.id.soda_card);
        soda_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.SODA);
                if(quantity == null) {
                    order.put(Globals.Item.SODA, 1);
                    Log.e("oi", order.toString());
                }
                else {
                    order.put(Globals.Item.SODA, quantity+1); //override
                    Log.e("new", order.toString());
                }
            }
        });

        popcorn_card = (CardView) view.findViewById(R.id.popcorn_card);
        popcorn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.POPCORN);
                if(quantity == null) {
                    order.put(Globals.Item.POPCORN, 1);
                    Log.e("oi", order.toString());
                }
                else {
                    order.put(Globals.Item.POPCORN, quantity+1); //override
                    Log.e("new", order.toString());
                }
            }
        });

        sandwich_card = (CardView) view.findViewById(R.id.sandwich_card);
        sandwich_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.SANDWICH);
                if(quantity == null) {
                    order.put(Globals.Item.SANDWICH, 1);
                    Log.e("oi", order.toString());
                }
                else {
                    order.put(Globals.Item.SANDWICH, quantity+1); //override
                    Log.e("new", order.toString());
                }
            }
        });

        return view;
    }
}
