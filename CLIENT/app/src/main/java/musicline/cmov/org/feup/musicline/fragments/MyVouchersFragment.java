package musicline.cmov.org.feup.musicline.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import musicline.cmov.org.feup.musicline.R;

public class MyVouchersFragment extends Fragment {

    public MyVouchersFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my_vouchers, container, false);

        return view;
    }
}
