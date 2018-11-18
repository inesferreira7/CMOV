package musicline.cmov.org.feup.musicline.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Hashtable;

import musicline.cmov.org.feup.musicline.R;
import musicline.cmov.org.feup.musicline.objects.Ticket;

public class ValidateTicketsActivity extends AppCompatActivity {
    ImageView qrCodeImageview;
    public final static int DIMENSION=500;
    String qrRequest;

    ArrayList<Ticket> tickets;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validate_tickets);

        Intent intent = getIntent();
        tickets = (ArrayList<Ticket>) intent.getSerializableExtra("Tickets");

        qrCodeImageview = (ImageView) findViewById(R.id.img_qr_code);

        qrRequest = new String();

        createRequest();
        generate();
    }

    void createRequest(){
        JSONObject request = new JSONObject();

        ArrayList<String> ids = new ArrayList<>();

        for (Ticket t : tickets){
            ids.add(t.getId());
        }

        try {
            request.put("customerId", tickets.get(0).getCustomerId());
            request.put("performanceDate", tickets.get(0).getPerformanceDate());
            request.put("performanceId", tickets.get(0).getPerformanceId());
            request.put("performanceName", tickets.get(0).getPerformanceName());
            request.put("ids", ids);
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
