package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.mymoviememoir.networkconnection.NetworkConnection;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.Locale;


public class SignUpSecondPage extends AppCompatActivity {
    private EditText addressEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText confirmEt;
    private EditText postcodeEt;
    private ImageView backButton;
    private Button signUpButton;
    private ImageView showHidePassword;
    private ImageView showHideConfirm;
    private Spinner stateSpinner;
    private int personCounter;
    private int credentialCounter;
    private static final String SHARED_PREF = "sharedPrefs";
    private static final String PERSON_COUNTER = "personCounter";
    private static final String CREDENTIAL_COUNTER = "credentialCounter";
    private boolean isShowPassClicked = false;
    private boolean isShowConfClicked = false;
    NetworkConnection networkConnection=null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_form_two);
        loadData();
        initView();
        //listeners start here
        showHidePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowPassClicked)
                {
                    passwordEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassClicked = true;
                }
                else
                {
                    passwordEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowPassClicked = false;
                }
            }
        });

        showHideConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isShowConfClicked)
                {
                    confirmEt.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowConfClicked = true;
                }
                else
                {
                    confirmEt.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    isShowConfClicked = false;
                }
            }
        });

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPreviousPage = new Intent(SignUpSecondPage.this, SignUpFirstPage.class);
                startActivity(toPreviousPage);
            }
        });

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] credentialData = createCredentialData();
                AddCredentialTask addCredentialTask = new AddCredentialTask();
                addCredentialTask.execute(credentialData);
            }
        });
    }

    protected void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        personCounter++;
        credentialCounter++;
        editor.putInt(PERSON_COUNTER, personCounter);
        editor.putInt(CREDENTIAL_COUNTER, credentialCounter);
        editor.apply();

    }

    protected void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREF, MODE_PRIVATE);
        personCounter = sharedPreferences.getInt(PERSON_COUNTER, 7);
        credentialCounter = sharedPreferences.getInt(CREDENTIAL_COUNTER, 17);
    }

    protected String[] createPersonData(){
        Intent fromPreviousPage = getIntent();
        Bundle data = fromPreviousPage.getExtras();
        String[] personData = new String[8];
        personData[0] = String.valueOf(personCounter);
        personData[1] = data.getString("firstName");
        personData[2] = data.getString("lastName");
        personData[3] = data.getString("dob");
        if (data.getString("gender").equals("male"))
        {
            personData[4] = "M";
        }
        else
        {
            personData[4] = "F";
        }
        personData[5] = addressEt.getText().toString();
        personData[6] = stateSpinner.getSelectedItem().toString();
        personData[7] = postcodeEt.getText().toString();
        return personData;
    }

    protected String[] createCredentialData(){
        String[] credentialData = new String[4];
        credentialData[0] = String.valueOf(credentialCounter);
        Date currentDate = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
        String dateInStr = formatter.format(currentDate);
        credentialData[1] = emailEt.getText().toString();
        credentialData[2] = getMd5Password(passwordEt.getText().toString());
        credentialData[3] = dateInStr;
        return credentialData;
    }

    private String getMd5Password(final String s) {
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

    private void initView(){
        addressEt = findViewById(R.id.address_form);
        emailEt = findViewById(R.id.email_form);
        passwordEt = findViewById(R.id.password_form);
        confirmEt = findViewById(R.id.confirm_form);
        postcodeEt = findViewById(R.id.postcode_form);
        backButton = findViewById(R.id.back_button2);
        signUpButton = findViewById(R.id.submit_button);
        showHidePassword = findViewById(R.id.show_password);
        showHideConfirm = findViewById(R.id.show_password2);
        stateSpinner = findViewById(R.id.state_spinner);
        networkConnection = new NetworkConnection();
    }

    private class AddCredentialTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            networkConnection.addPerson(createPersonData());
            networkConnection.addCredential(params);
            saveData();
            String message = "New credential data has been added!";
            return message;
        };
        @Override
        protected void onPostExecute(String result) {
            Intent toMainPage = new Intent(SignUpSecondPage.this, MainActivity.class);
            startActivity(toMainPage);
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
        };
    }
}
