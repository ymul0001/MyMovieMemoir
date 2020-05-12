package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class LoginForm extends AppCompatActivity {
    private EditText usernameEt;
    private EditText passwordEt;
    private Button submitButton;
    private ImageView showHideButton;
    boolean isPasswordShown = false;
    NetworkConnection networkConnection=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_form);
        submitButton = findViewById(R.id.submit_button);
        showHideButton = findViewById(R.id.show_password);
        usernameEt = findViewById(R.id.login_username);
        passwordEt = findViewById(R.id.login_password);
        networkConnection = new NetworkConnection();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateCredentials validateCredentials = new ValidateCredentials();
                validateCredentials.execute();
            }
        });
        showHideButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isPasswordShown)
                {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isPasswordShown = true;
                }
                else
                {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isPasswordShown = false;
                }
            }
        });
    }

    private class ValidateCredentials extends AsyncTask<Void, Void, String> {

        protected String getMd5Password(final String s) {
            final String MD5MODULE = "MD5";
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest
                        .getInstance(MD5MODULE);
                digest.update(s.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuilder hexString = new StringBuilder();
                for (byte aMessageDigest : messageDigest) {
                    String h = Integer.toHexString(0xFF & aMessageDigest);
                    while (h.length() < 2)
                        h = "0" + h;
                    hexString.append(h);
                }
                return hexString.toString();

            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }
            return "";
        }

        @Override
        protected String doInBackground(Void... params) {
            String jsonResult = networkConnection.getAllCredentials();
            return jsonResult;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            JSONArray jsonArray = null;
            ArrayList<String> username = new ArrayList<>();
            ArrayList<String> password = new ArrayList<>();
            try {
                jsonArray = new JSONArray(jsonResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < jsonArray.length(); i++){
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    password.add(obj.get("credHash").toString());
                    username.add(obj.get("credUsername").toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            String hash = getMd5Password(passwordEt.getText().toString());
            Intent intent = new Intent(LoginForm.this, MainActivity.class);
            if(username.indexOf(usernameEt.getText().toString()) == -1 || password.indexOf(hash) == -1)
            {
                Toast.makeText(getApplicationContext(), "You have provided wrong credentials! Please try again",
                        Toast.LENGTH_LONG).show();
            }
            else
            {
                startActivity(intent);
                Toast.makeText(getApplicationContext(), "Successfully logged in to the system!",
                        Toast.LENGTH_LONG).show();
            }
        }
    }
}
