package com.example.mymoviememoir;

import androidx.appcompat.app.AppCompatActivity;


import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;




public class MainActivity extends AppCompatActivity {
    private TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        Intent fromLogin = getIntent();
        Bundle bundle = fromLogin.getExtras();
        tv.setText("Hello " + bundle.getString("firstName"));
    }
}
