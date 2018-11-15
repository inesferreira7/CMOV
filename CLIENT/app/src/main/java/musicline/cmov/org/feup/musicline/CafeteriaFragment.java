package musicline.cmov.org.feup.musicline;

import android.content.Context;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;

public class CafeteriaFragment extends Fragment{

    HashMap<Globals.Item, Integer> order;
    CardView coffee_card, soda_card, popcorn_card, sandwich_card;
    ListView order_list;
    MyAdapter order_adapter;
    double order_total;
    TextView order_total_text;

    public CafeteriaFragment() { }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);
        order = new HashMap<>();
        order_list = (ListView) view.findViewById(R.id.order_list);
        order_adapter = new MyAdapter(order);
        order_list.setAdapter(order_adapter);

        order_total = 0;
        order_total_text= (TextView)view.findViewById(R.id.order_total);
        order_total_text.setTextColor(getResources().getColor(R.color.colorPrimary));
        order_total_text.setTypeface(Typeface.DEFAULT_BOLD);

        coffee_card = (CardView) view.findViewById(R.id.coffee_card);

        coffee_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Key existence check
                Integer quantity = order.get(Globals.Item.Coffee);
                if(quantity == null) {
                    order.put(Globals.Item.Coffee, 1);
                    order_total+= Globals.COFFEE_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
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
        soda_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.Soda);
                if(quantity == null) {
                    order.put(Globals.Item.Soda, 1);
                    order_total+= Globals.SODA_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
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
        popcorn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.Popcorn);
                if(quantity == null) {
                    order.put(Globals.Item.Popcorn, 1);
                    order_total+= Globals.POPCORN_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
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
        sandwich_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Integer quantity = order.get(Globals.Item.Sandwich);
                if(quantity == null) {
                    order.put(Globals.Item.Sandwich, 1);
                    order_total+= Globals.SANDWICH_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                }
                else {
                    order.put(Globals.Item.Sandwich, quantity+1); //override
                    order_total+= Globals.SANDWICH_PRICE;
                    order_total_text.setText("TOTAL = " + String.valueOf(order_total) + "€");
                }

                order_adapter.updateList(order);
            }
        });

        return view;
    }
}
