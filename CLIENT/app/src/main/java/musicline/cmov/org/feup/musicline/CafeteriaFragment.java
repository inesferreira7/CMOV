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

    CardView coffee_card, soda_card, popcorn_card, sandwich_card;
    public CafeteriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        coffee_card = (CardView) view.findViewById(R.id.coffee_card);
        coffee_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coffee", "Clicked on coffee");
            }
        });

        soda_card = (CardView) view.findViewById(R.id.soda_card);
        soda_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coffee", "Clicked on soda");
            }
        });

        popcorn_card = (CardView) view.findViewById(R.id.popcorn_card);
        popcorn_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coffee", "Clicked on popcorn");
            }
        });

        sandwich_card = (CardView) view.findViewById(R.id.sandwich_card);
        sandwich_card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coffee", "Clicked on sandwich");
            }
        });

        return view;
    }
}
