package musicline.cmov.org.feup.musicline.adapters;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;
import java.util.Map;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Show;
import musicline.cmov.org.feup.musicline.utils.Globals;

public class MyAdapter extends BaseAdapter{

    private final ArrayList orderData;

    public MyAdapter(Map<Globals.Item, Integer>order){
        orderData = new ArrayList();
        orderData.addAll(order.entrySet());
    }

    public void updateList(Map<Globals.Item, Integer> data) {
        orderData.clear();
        orderData.addAll(data.entrySet());
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orderData.size();
    }

    @Override
    public Map.Entry<Globals.Item, Integer> getItem(int i) {
        return (Map.Entry) orderData.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final View result;

        if(convertView == null){
            result = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_adapter, parent, false);
        }
        else {
            result = convertView;
        }

        Map.Entry<Globals.Item, Integer> item = getItem(position);

            ((TextView) result.findViewById(R.id.order_product)).setTextColor(Color.parseColor("#263238"));
            ((TextView) result.findViewById(R.id.order_product)).setTypeface(Typeface.DEFAULT_BOLD);
            ((TextView) result.findViewById(R.id.order_product)).setText(item.getValue().toString() + " x    " + item.getKey().toString());

        return result;
    }
}
