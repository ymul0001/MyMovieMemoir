package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUpSecondPage extends AppCompatActivity {
    private EditText addressEt;
    private EditText emailEt;
    private EditText passwordEt;
    private EditText confirmEt;
    private ImageView backButton;
    private Button signUpButton;
    private ImageView showHidePassword;
    private ImageView showHideConfirm;
    private boolean isShowPassClicked = false;
    private boolean isShowConfClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_form_two);
        addressEt = findViewById(R.id.address_form);
        emailEt = findViewById(R.id.email_form);
        passwordEt = findViewById(R.id.password_form);
        confirmEt = findViewById(R.id.confirm_form);
        backButton = findViewById(R.id.back_button2);
        signUpButton = findViewById(R.id.submit_button);
        showHidePassword = findViewById(R.id.show_password);
        showHideConfirm = findViewById(R.id.show_password2);

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

    }
}
