package com.example.nuno.terminal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

public class OrderValidated extends AppCompatActivity {

    TextView order_number, nif, total_price;
    JSONArray products, vouchers;
    LinearLayout margin_layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_validated);

        margin_layout = findViewById(R.id.margin_layout);

        String number = getIntent().getStringExtra("ORDER_NUMBER");
        order_number = findViewById(R.id.order_number);
        order_number.setText("Order number : " + number);

        try {
            products = new JSONArray(getIntent().getStringExtra("ORDER_PRODUCTS"));
            vouchers = new JSONArray(getIntent().getStringExtra("ORDER_VOUCHERS"));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for(int i = 0; i < products.length(); i++) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.HORIZONTAL);
            TextView product = new TextView(getApplicationContext());
            TextView quantity = new TextView(getApplicationContext());
            try {
                JSONObject p = products.getJSONObject(i);
                product.setText(p.getString("product"));
                Log.i("Product", p.getString("product"));
                layout.addView(product);
                quantity.setText("  " + Integer.toString(p.getInt("quantity")) +"x");
                Log.i("Qtd", p.getString("quantity"));
                layout.addView(quantity);
                margin_layout.addView(layout);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Log.i("Vouchers", vouchers.toString());

        TextView voucher_text = new TextView(getApplicationContext());
        voucher_text.setText("Vouchers: ");

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(0,15,0,00);
        voucher_text.setLayoutParams(params);

        margin_layout.addView(voucher_text);
        for(int i = 0; i < vouchers.length(); i++) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setOrientation(LinearLayout.VERTICAL);
            TextView voucher = new TextView(getApplicationContext());

            try {
                String v = vouchers.getString(i);
                voucher.setText(v);
                Log.i("Type", v);
                layout.addView(voucher);
                margin_layout.addView(layout);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        String nif = getIntent().getStringExtra("NIF");
        this.nif = findViewById(R.id.order_nif);
        this.nif.setText("NIF : " + nif);

        String totalPrice = getIntent().getStringExtra("TOTAL_PRICE");
        this.total_price = findViewById(R.id.order_price);
        this.total_price.setText("Total price : " + totalPrice);
    }
}
