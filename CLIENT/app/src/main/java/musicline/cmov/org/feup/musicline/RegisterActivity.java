package musicline.cmov.org.feup.musicline;

import android.app.DownloadManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.google.common.collect.Range;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText name, username, email, password, confirmPassword, nif, cardNumber, cardValidity;
    Button register;
    AwesomeValidation validation;
    RadioGroup type;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        validation = new AwesomeValidation(ValidationStyle.BASIC);

        updateUI();
    }

        //Handling validation of card validity
        TextWatcher validityWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            String working = s.toString();
            boolean isValid = true;
            if (working.length() == 2 && before == 0) {
                if (Integer.parseInt(working) < 1 || Integer.parseInt(working) > 12) {
                    isValid = false;
                } else {
                    working += "/";
                    cardValidity.setText(working);
                    cardValidity.setSelection(working.length());
                }
            } else if (working.length() == 5 && before == 0) {
                String year = working.substring(3);
                if (Integer.parseInt(year) < 18) {
                    isValid = false;
                }
            } else if (working.length() != 5) {
                isValid = false;
            }
            if (isValid) {
                cardValidity.setError(null);
            } else {
                cardValidity.setError("Enter a valid validity");
            }
        }

        @Override
        public void afterTextChanged(Editable editable) { }
    };

    private void updateUI() {

        name = (EditText) findViewById(R.id.register_name);
        username = (EditText) findViewById(R.id.register_username);
        email = (EditText) findViewById(R.id.register_email);
        password = (EditText) findViewById(R.id.register_password);
        confirmPassword = (EditText) findViewById(R.id.register_confirmPassword);
        nif = (EditText) findViewById(R.id.register_nif);
        register = (Button) findViewById(R.id.register_button);
        type = (RadioGroup) findViewById(R.id.register_cardType);
        cardNumber = (EditText) findViewById(R.id.register_cardNumber);
        cardValidity = (EditText) findViewById(R.id.register_cardValidity);
        cardValidity.addTextChangedListener(validityWatcher);

        //Validation area
        validation.addValidation(RegisterActivity.this, R.id.register_name, "[a-zA-Z\\s]+", R.string.error_name);
        validation.addValidation(RegisterActivity.this, R.id.register_username, "^[a-zA-Z0-9._-]{3,}$", R.string.error_username);
        validation.addValidation(RegisterActivity.this, R.id.register_email, Patterns.EMAIL_ADDRESS, R.string.error_email);
        validation.addValidation(RegisterActivity.this, R.id.register_password, "^[A-Za-z0-9]{8,}$", R.string.error_password);
        validation.addValidation(RegisterActivity.this, R.id.register_confirmPassword, R.id.register_password, R.string.error_confirmPass);
        validation.addValidation(RegisterActivity.this, R.id.register_nif, "^\\d{9}$", R.string.error_nif);
        validation.addValidation(RegisterActivity.this, R.id.register_cardType, new CustomValidation() {
            @Override
            public boolean compare(ValidationHolder validationHolder) {
                if (((RadioGroup) validationHolder.getView()).getCheckedRadioButtonId() == -1) {
                    return false;
                } else {
                    return true;
                }
            }
        }, new CustomValidationCallback() {
            @Override
            public void execute(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) findViewById(R.id.register_warning);
                textViewError.setError(validationHolder.getErrMsg());
            }
        }, new CustomErrorReset() {
            @Override
            public void reset(ValidationHolder validationHolder) {
                TextView textViewError = (TextView) findViewById(R.id.register_warning);
                textViewError.setError(null);
            }
        }, R.string.error_type);

        validation.addValidation(RegisterActivity.this, R.id.register_cardNumber, "^\\d{16}$", R.string.error_cardNumber);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (validation.validate()) {
                    Toast.makeText(RegisterActivity.this, "Data received successfully!", Toast.LENGTH_SHORT).show();
                    try {
                        String publicKey = generateKeyPair();
                        //TODO Generate JSON to send request

                        Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private void request(String method, String parameter) throws Exception {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Globals.url + "/customers";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("Response", response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        queue.add(stringRequest);
    }

    private String generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(512);
        byte[] publicKey = keyGen.genKeyPair().getPublic().getEncoded();

        String retString = new String();
        for (int i = 0; i < publicKey.length; i++) {
            retString += Integer.toString(publicKey[i]);
        }
        return retString;
    }
}
