package musicline.cmov.org.feup.musicline.activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Order;
import musicline.cmov.org.feup.musicline.objects.Ticket;
import musicline.cmov.org.feup.musicline.objects.Voucher;

public class ValidateOrderActivity extends AppCompatActivity {
    ImageView qrCodeImageview;
    public final static int DIMENSION=500;
    String qrRequest;
    Order order;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_order);

        Intent intent = getIntent();
        order = (Order) intent.getSerializableExtra("Order");

        qrCodeImageview = (ImageView) findViewById(R.id.order_qr_code);

        qrRequest = new String();

        createRequest();
        generate();
    }

    void createRequest(){
        JSONObject request = new JSONObject();
        JSONArray vouchers = new JSONArray();
        JSONArray products = new JSONArray();

        Iterator it = order.getProducts().entrySet().iterator();
        while(it.hasNext()){
            Map.Entry pair = (Map.Entry)it.next();
            JSONObject product = new JSONObject();

            try {
                product.put("product", (String)pair.getKey());
                product.put("quantity", (Integer)pair.getValue());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            products.put(product);
        }

        for(int i = 0; i < order.getVouchers().size(); i++){
            JSONObject voucher = new JSONObject();

            try {
                voucher.put("voucherId", order.getVouchers().get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }

            vouchers.put(voucher);
        }

        try {
            request.put("id", order.getId());
            request.put("customerId", order.getCustomerId());
            request.put("products", products);
            request.put("vouchers", vouchers);
            request.put("totalPrice", Double.toString(order.getTotalPrice()));
            request.put("validated", order.isValidated());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        qrRequest = request.toString();
    }

    void generate() {
        new Thread(new convertToQR(qrRequest)).start();
    }

    class convertToQR implements Runnable {
        String content;

        convertToQR(String value) {
            content = value;
        }

        @Override
        public void run() {
            final Bitmap bitmap;
            final String errorMsg;
            try {
                bitmap = encodeAsBitmap(content);
                runOnUiThread(new Runnable() {  // runOnUiThread method used to do UI task in main thread.
                    @Override
                    public void run() {
                        qrCodeImageview.setImageBitmap(bitmap);
                    }
                });
            }
            catch (WriterException e) {
                errorMsg = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }
    }

    Bitmap encodeAsBitmap(String str) throws WriterException {
        BitMatrix result;

        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        try {
            result = new MultiFormatWriter().encode(str, BarcodeFormat.QR_CODE, DIMENSION, DIMENSION, hints);
        }
        catch (IllegalArgumentException iae) {
            // Unsupported format
            return null;
        }

        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                pixels[offset + x] = result.get(x, y) ? getResources().getColor(R.color.colorPrimary):getResources().getColor(R.color.white);
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, w, 0, 0, w, h);
        return bitmap;
    }
}
