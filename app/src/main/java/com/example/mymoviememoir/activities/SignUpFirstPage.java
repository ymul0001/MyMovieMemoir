package com.example.mymoviememoir.activities;

import androidx.appcompat.app.AppCompatActivity;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.mymoviememoir.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class SignUpFirstPage extends AppCompatActivity {
    private ImageView backButton;
    private Calendar dobCalendar;
    private ImageView dobButton;
    private EditText dobEt;
    private Button nextButton;
    private RadioGroup genderGroup;
    private RadioButton genderRadio;
    private EditText firstNameEt;
    private EditText lastNameEt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup_form_one);
        backButton = findViewById(R.id.back_button);
        dobButton = findViewById(R.id.dob_button);
        dobCalendar = Calendar.getInstance();
        dobEt = findViewById(R.id.dob_form);
        nextButton = findViewById(R.id.next_button);
        genderGroup = findViewById(R.id.gender_group);
        firstNameEt = findViewById(R.id.firstname_form);
        lastNameEt = findViewById(R.id.lastname_form);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month,
                                  int day) {
                String datePattern = "yyyy-MM-dd'T'HH:mm:ssXXX";
                dobCalendar.set(Calendar.YEAR, year);
                dobCalendar.set(Calendar.MONTH, month);
                dobCalendar.set(Calendar.DAY_OF_MONTH, day);
                SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                dobEt.setText(dateFormat.format(dobCalendar.getTime()));
            }
        };

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginIntent = new Intent(SignUpFirstPage.this, LoginForm.class);
                startActivity(loginIntent);
            }
        });

        dobButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(SignUpFirstPage.this, date, dobCalendar
                        .get(Calendar.YEAR), dobCalendar.get(Calendar.MONTH),
                        dobCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int genderId = genderGroup.getCheckedRadioButtonId();
                genderRadio = findViewById(genderId);
                if (firstNameEt.getText().toString().trim().length() > 0 && lastNameEt.getText().toString().trim().length() > 0)
                {
                    if (dobEt.getText().toString().trim().length() > 0)
                    {
                        Intent toNextSignupForm = new Intent(SignUpFirstPage.this, SignUpSecondPage.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("firstName", firstNameEt.getText().toString());
                        bundle.putString("lastName", lastNameEt.getText().toString());
                        bundle.putString("dob", dobEt.getText().toString());
                        bundle.putString("gender", genderRadio.getText().toString());
                        toNextSignupForm.putExtras(bundle);
                        startActivity(toNextSignupForm);
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), "Please fill all the forms first",
                                Toast.LENGTH_LONG).show();
                    }
                }
                else
                {
                    Toast.makeText(getApplicationContext(), "Please fill all the forms first",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}
