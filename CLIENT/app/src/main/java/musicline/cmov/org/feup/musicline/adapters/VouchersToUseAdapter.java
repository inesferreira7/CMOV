package musicline.cmov.org.feup.musicline.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Voucher;
import musicline.cmov.org.feup.musicline.utils.Globals;

public class VouchersToUseAdapter<T> extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<T> list;
    private SparseBooleanArray spa;
    private Map<String, Integer> products;

    public VouchersToUseAdapter(Context context, ArrayList<T> list, HashMap<String, Integer> products) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.spa = new SparseBooleanArray();
        this.list = new ArrayList<>();
        this.list = list;
        this.products = products;
    }

    @Override
    public int getCount() {
        return this.list.size();
    }

    @Override
    public Object getItem(int i) {
        return this.list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public ArrayList<T> getCheckedItems() {
        ArrayList<T> temp = new ArrayList<>();
        for (int i = 0; i < this.list.size(); i++) {
            if (this.spa.get(i)) {
                temp.add(this.list.get(i));
            }
        }
        return temp;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null) {
            convertView = this.inflater.inflate(R.layout.vouchers_to_use_adapter, null);
        }

        Voucher v = (Voucher) this.list.get(position);

        ImageView icon = (ImageView) convertView.findViewById(R.id.voucher_to_use_icon);
        TextView type = (TextView) convertView.findViewById(R.id.voucher_to_use_type);

        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.chkEnable_toUse);
        checkbox.setTag(position);
        checkbox.setChecked(this.spa.get(position));
        checkbox.setOnCheckedChangeListener(checkedChangeListener);

        if (v.getType().equals("Coffee")) {
            icon.setImageResource(R.drawable.coffee_voucher);
            type.setText("Free Coffee");

            if(v.isUsed() || !products.containsKey(Globals.Item.Coffee))checkbox.setEnabled(false);
            else checkbox.setEnabled(true);
        }

        if (v.getType().equals("Popcorn")) {
            icon.setImageResource(R.drawable.popcorn_voucher);
            type.setText("Free Popcorn");

            if(v.isUsed() || !products.containsKey(Globals.Item.Popcorn))checkbox.setEnabled(false);
            else checkbox.setEnabled(true);
        }

        if (v.getType().equals("5%")) {
            icon.setImageResource(R.drawable.percentage_voucher);
            type.setText("5% Discount");

            if(v.isUsed())checkbox.setEnabled(false);
            else checkbox.setEnabled(true);
        }

        return convertView;
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            Voucher v = (Voucher) list.get((Integer) compoundButton.getTag());

            if (checked) {
                spa.put((Integer) compoundButton.getTag(), checked);
            } else {
                spa.delete((Integer) compoundButton.getTag());
            }

            if (spa.size() == 3) {
                Toast.makeText(context, "Select only up to 2 vouchers!", Toast.LENGTH_LONG).show();
                compoundButton.setChecked(false);
                spa.delete((Integer) compoundButton.getTag());
            }
        }
    };
}

