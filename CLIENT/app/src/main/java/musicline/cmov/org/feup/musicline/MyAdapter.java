package musicline.cmov.org.feup.musicline;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyAdapter extends BaseAdapter{

    private final ArrayList orderData;

    public MyAdapter(Map<Globals.Item, Integer>order){
        orderData = new ArrayList();
        orderData.addAll(order.entrySet());
    }

    public void updateList(HashMap<Globals.Item, Integer> data) {
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

        /*
        if(item.getValue() > 0) {
            result.setVisibility(View.VISIBLE);
            */
            ((TextView) result.findViewById(R.id.order_product)).setTextColor(Color.parseColor("#263238"));
            ((TextView) result.findViewById(R.id.order_product)).setText(item.getValue().toString() + " x    " + item.getKey().toString());
            /*
        }
        else{
            result.setVisibility(View.GONE);
        }*/

        return result;
    }
}
