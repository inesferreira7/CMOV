package musicline.cmov.org.feup.musicline.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Ticket;

public class TicketAdapter<T> extends BaseAdapter {
    private Context context;
    private LayoutInflater inflater;
    private ArrayList<T> list;
    private SparseBooleanArray spa;
    private String date;

    public TicketAdapter(Context context, ArrayList<T> list) {
        this.context = context;
        this.inflater = LayoutInflater.from(this.context);
        this.spa = new SparseBooleanArray();
        this.list = new ArrayList<>();
        this.list = list;
        this.date = "";
    }

    public void setDate(String date) {this.date = date;}

    public String getDate() { return this.date;}

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
            convertView = this.inflater.inflate(R.layout.ticket_adapter, null);
        }

        Ticket ticket = (Ticket) this.list.get(position);

        TextView performanceName = ((TextView) convertView.findViewById(R.id.performance_name));
        performanceName.setText(ticket.getPerformanceName());

        TextView performanceDate = ((TextView) convertView.findViewById(R.id.performance_date));
        performanceDate.setText(ticket.getPerformanceDate());

        CheckBox checkbox = (CheckBox) convertView.findViewById(R.id.chkEnable);
        checkbox.setTag(position);
        checkbox.setChecked(this.spa.get(position));
        checkbox.setOnCheckedChangeListener(checkedChangeListener);

        if(ticket.isUsed()) checkbox.setEnabled(false);
        else checkbox.setEnabled(true);

        return convertView;
    }

    CompoundButton.OnCheckedChangeListener checkedChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
            Ticket t = (Ticket) list.get((Integer) compoundButton.getTag());

            if(checked){
                spa.put((Integer) compoundButton.getTag(), checked);
            }else{
                spa.delete((Integer) compoundButton.getTag());
            }

            if(spa.size() == 5){
                Toast.makeText(context, "Select only up to 4 tickets to validate!", Toast.LENGTH_LONG).show();
                compoundButton.setChecked(false);
                spa.delete((Integer) compoundButton.getTag());
            }
        }
    };

}
