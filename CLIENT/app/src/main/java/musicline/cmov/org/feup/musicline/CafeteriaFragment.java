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

    CardView card_coffee;
    public CafeteriaFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_cafeteria, container, false);

        card_coffee = (CardView) view.findViewById(R.id.card_coffee);
        card_coffee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("coffee", "Clicked on coffee");
            }
        });

        return view;
    }
}
