package musicline.cmov.org.feup.musicline;

import android.app.DownloadManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.security.KeyPairGeneratorSpec;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.basgeekball.awesomevalidation.AwesomeValidation;
import com.basgeekball.awesomevalidation.ValidationHolder;
import com.basgeekball.awesomevalidation.ValidationStyle;
import com.basgeekball.awesomevalidation.utility.custom.CustomErrorReset;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidation;
import com.basgeekball.awesomevalidation.utility.custom.CustomValidationCallback;
import com.google.common.collect.Range;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

public class RegisterActivity extends AppCompatActivity {

    EditText name, username, email, password, confirmPassword, nif, cardNumber, cardValidity;
    Button register;
    AwesomeValidation validation;
    RadioGroup type;
    RadioButton type_chosen;


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
        email = (EditText) findViewById(R.id.register_email);
        nif = (EditText) findViewById(R.id.register_nif);
        register = (Button) findViewById(R.id.register_button);
        type = (RadioGroup) findViewById(R.id.register_cardType);
        cardNumber = (EditText) findViewById(R.id.register_cardNumber);
        cardValidity = (EditText) findViewById(R.id.register_cardValidity);
        cardValidity.addTextChangedListener(validityWatcher);

        //Validation area
        validation.addValidation(RegisterActivity.this, R.id.register_name, "[a-zA-Z\\s]+", R.string.error_name);
        validation.addValidation(RegisterActivity.this, R.id.register_email, Patterns.EMAIL_ADDRESS, R.string.error_email);
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
                int selectedId = type.getCheckedRadioButtonId();
                type_chosen = (RadioButton)findViewById(selectedId);

                if (validation.validate()) {
                    Toast.makeText(RegisterActivity.this, "Data received successfully!", Toast.LENGTH_SHORT).show();
                    try {
                        KeyPair keyPair = generateKeyPair();

                        if (keyPair != null) {
                            PublicKey public_key = keyPair.getPublic();

                            JSONObject customer = new JSONObject();
                            customer.put("name", name.getText().toString());
                            customer.put("nif", nif.getText().toString());
                            JSONObject credit_card = new JSONObject();
                            credit_card.put("card_type", type_chosen.getText().toString());
                            credit_card.put("card_number", cardNumber.getText().toString());
                            credit_card.put("validity", cardValidity.getText().toString());
                            customer.put("credit_card", credit_card);
                            customer.put("key", byteToString(public_key.getEncoded()));

                            registerCustomer(customer);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(RegisterActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private KeyPair generateKeyPair() {
        KeyStore keystore = null;
        try {
            keystore = KeyStore.getInstance("AndroidKeyStore");
            keystore.load(null);
            String alias = "private_key";

            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this)
                    .setAlias(alias)
                    .setSubject(new X500Principal("CN=Sample Name, O=Android Authority"))
                    .setSerialNumber(BigInteger.ONE)
                    .setStartDate(Calendar.getInstance().getTime())
                    .setEndDate(Calendar.getInstance().getTime())
                    .build();

            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            keyGen.initialize(spec);

            KeyPair keyPair = keyGen.genKeyPair();

            return keyPair;
        } catch (KeyStoreException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CertificateException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }

        return null;
    }

    private String byteToString(byte[] array){
        String array_string = new String();

        for(int i = 0; i < array.length; i++) {
            array_string += Integer.toString(array[i]);
        }

        return array_string;
    }

    private void registerCustomer(final JSONObject customer) {
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = Globals.URL + "/customer/register";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, customer, new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    Log.i("Response", response.toString());
                    try {
                        String uuid = response.getString("uuid");

                        SharedPreferences.Editor editor = getSharedPreferences(Globals.PREFERENCES_NAME, MODE_PRIVATE).edit();
                        editor.putString("uuid", uuid);
                        editor.apply();

                        Intent intent = new Intent(getApplicationContext(), HomepageActivity.class);
                        startActivity(intent);
                        finish();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e("Error API call", error.toString());
                }
            });

        queue.add(request);
    }
}