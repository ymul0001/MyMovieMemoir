package com.example.mymoviememoir.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.adapter.DashboardAdapter;
import com.example.mymoviememoir.entities.Cinema;
import com.example.mymoviememoir.fragments.DashboardFragment;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class CinemaForm extends AppCompatActivity {
    private EditText cinemaNameEt;
    private EditText cinemaAddressEt;
    private EditText cinemaPostcodeEt;
    private Button addButton;
    private int cinemaId;
    private ArrayList<String> listOfCinemas;
    NetworkConnection networkConnection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cinema_form);
        cinemaNameEt = findViewById(R.id.cinemaname_form);
        cinemaAddressEt = findViewById(R.id.cinemaaddress_form);
        cinemaPostcodeEt = findViewById(R.id.cinemapostcode_form);
        addButton  = findViewById(R.id.addcinema_button);
        networkConnection = new NetworkConnection();
        listOfCinemas = new ArrayList<>();
        cinemaId = loadData();
        //get all cinema names from the database
        GetAllCinemaNameTask getAllCinemaNameTask = new GetAllCinemaNameTask();
        getAllCinemaNameTask.execute();

        //receive intent from movie details page
        Intent fromMovieDetail = getIntent();


        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cinemaName = cinemaNameEt.getText().toString();
                String cinemaAddress = cinemaAddressEt.getText().toString();
                String cinemaPostcode = cinemaPostcodeEt.getText().toString();
                ArrayList<String> result = new ArrayList<>();
                result.add(cinemaName.trim());
                result.add(cinemaAddress.trim());
                result.add(cinemaPostcode.trim());
                if (result.indexOf("") != -1)
                {
                    Toast.makeText(getApplicationContext(), "Form has not been completed yet!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    if(listOfCinemas.indexOf(cinemaName.toLowerCase()) != -1)
                    {
                        Toast.makeText(getApplicationContext(), "Cinema is already listed!",
                                Toast.LENGTH_LONG).show();
                    }
                    else {
                        String[] newCinema = new String[]{String.valueOf(cinemaId), result.get(0), result.get(1), result.get(2)};
                        saveData();
                        AddCinemaTask addCinemaTask = new AddCinemaTask();
                        addCinemaTask.execute(newCinema);
                    }
                }
            }
        });
    }

    public void saveData(){
        SharedPreferences sf = getSharedPreferences("cinemaCounterPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sf.edit();
        cinemaId++;
        Log.d("string", String.valueOf(cinemaId));
        editor.putInt("cinemaId", cinemaId);
        editor.apply();
    }

    public int loadData() {
        SharedPreferences sf = getSharedPreferences("cinemaCounterPrefs", MODE_PRIVATE);
        return sf.getInt("cinemaId", 1011);
    }

    private class GetAllCinemaNameTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = networkConnection.getAllCinemas();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = null;
                try {
                    obj = jsonArray.getJSONObject(i);
                    listOfCinemas.add(obj.getString("cinemaName").toLowerCase());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "hello World!";
        }

        @Override
        protected void onPostExecute(String jsonResult) {

        }
    }


    private class AddCinemaTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params){
            Log.d("string", params.toString());
            networkConnection.addCinema(params);
            String message = "Successfully added a cinema to the list!";
            return message;
        };
        @Override
        protected void onPostExecute(String result) {
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
            restartToMainActivity();
        };
    }

    private void restartToMainActivity(){
        SharedPreferences sf = getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
        Intent intent = new Intent(CinemaForm.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("personId", sf.getInt("personId", 0));
        bundle.putString("firstName", sf.getString("firstName", ""));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
