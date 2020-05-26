package com.example.mymoviememoir.activities;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.Cinema;
import com.example.mymoviememoir.entities.CinemaId;
import com.example.mymoviememoir.entities.Memoir;
import com.example.mymoviememoir.entities.PersonId;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MemoirForm extends AppCompatActivity{
    private TextView movieTitleTv;
    private TextView movieReleaseTv;
    private ImageView moviePosterIv;
    private EditText movieWatchdateEt;
    private EditText movieWatchtimeEt;
    private EditText movieCommentEt;
    private ImageView datePickerIv;
    private ImageView timePickerIv;
    private Spinner cinemaSpinner;
    private RatingBar ratingBar;
    private Button addMemoirButton;
    private Button addCinemaButton;
    private List<String> listOfCinemas;
    private int memoirId;
    private int personId;
    private int cinemaId;
    private String selectedCinema;

    NetworkConnection networkConnection;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoir_form);
        listOfCinemas = new ArrayList<>();
        memoirId = loadData();

        //load personId from sharedPreferences
        SharedPreferences sf = getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
        personId = sf.getInt("personId", 0);

        //init view
        initView();

        //loading intent
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        bundle.getString("releaseDate");
        bundle.getString("backdropPath");

        //insert the initial view
        movieTitleTv.setText(bundle.getString("movieTitle"));
        movieReleaseTv.setText(bundle.getString("releaseDate"));
        Picasso.get().load("https://image.tmdb.org/t/p/original" + bundle.getString("backdropPath")).into(moviePosterIv);

        //initiate the spinner adapter data from API
        GetCinemaSpinnerData getCinemaSpinnerData = new GetCinemaSpinnerData();
        getCinemaSpinnerData.execute();

        cinemaSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCinema = cinemaSpinner.getSelectedItem().toString();
                String[] cinemaDetails = selectedCinema.split(",");
                GetCinemaByNameandPostalTask getCinemaByNameandPostalTask = new GetCinemaByNameandPostalTask();
                getCinemaByNameandPostalTask.execute(cinemaDetails);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        //listeners start here
        addMemoirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //store all data for the memoir
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault());
                SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
                String movieWatchDate = movieWatchdateEt.getText().toString();
                String movieWatchTime = movieWatchDate + 'T' + movieWatchtimeEt.getText().toString() + ":00";
                try {
                    Log.d("string", formatter.format(timeFormatter.parse(movieWatchTime)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                String movieComment = movieCommentEt.getText().toString();
                float rating = ratingBar.getRating();

                //validate the memoir if it is not blank
                String[] tempData = new String[]{movieWatchDate,movieWatchTime, selectedCinema, movieComment, Float.toString(rating)};

                List<String> data = Arrays.asList(tempData);
                if(data.indexOf("") != -1) {
                    Toast.makeText(getApplicationContext(), "Form has not been completed yet!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    Memoir[] memoirs = new Memoir[1];
                    Memoir newMemoir = null;
                    try {
                        newMemoir = new Memoir(memoirId, movieTitleTv.getText().toString(), formatter.format(dateFormatter.parse(movieReleaseTv.getText().toString())),formatter.format(dateFormatter.parse(movieWatchDate)),formatter.format(timeFormatter.parse(movieWatchTime)),movieComment, rating, new PersonId(personId),new CinemaId(cinemaId));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    memoirs[0] = newMemoir;
                    saveData();
                    AddMemoirTask addMemoirTask = new AddMemoirTask();
                    addMemoirTask.execute(memoirs);
                }
            }
        });

        addCinemaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCinemaForm = new Intent(MemoirForm.this, CinemaForm.class);
                startActivity(toCinemaForm);
            }
        });

        timePickerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar currentTime = Calendar.getInstance();
                int hour = currentTime.get(Calendar.HOUR_OF_DAY);
                int minute = currentTime.get(Calendar.MINUTE);
                TimePickerDialog timePicker;
                timePicker = new TimePickerDialog(MemoirForm.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        movieWatchtimeEt.setText(String.format("%02d:%02d", selectedHour, selectedMinute));
                    }
                }, hour, minute, true);
                timePicker.setTitle("Select watch time: ");
                timePicker.show();
            }
        });

        datePickerIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar currentDate = Calendar.getInstance();
                new DatePickerDialog(MemoirForm.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month,
                                          int day) {
                        String datePattern = "yyyy-MM-dd";

                        currentDate.set(Calendar.YEAR, year);
                        currentDate.set(Calendar.MONTH, month);
                        currentDate.set(Calendar.DAY_OF_MONTH, day);
                        SimpleDateFormat dateFormat = new SimpleDateFormat(datePattern);
                        movieWatchdateEt.setText(dateFormat.format(currentDate.getTime()));
                    }
                }, currentDate
                        .get(Calendar.YEAR), currentDate.get(Calendar.MONTH),
                       currentDate.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


    }

    //method to initiate the view
    private void initView(){
        movieTitleTv = findViewById(R.id.movie_title);
        movieReleaseTv = findViewById(R.id.release_date);
        moviePosterIv = findViewById(R.id.movie_poster);
        movieWatchdateEt = findViewById(R.id.watchdate_form);
        movieWatchtimeEt = findViewById(R.id.watchtime_form);
        movieCommentEt = findViewById(R.id.cinemacomment_form);
        ratingBar = findViewById(R.id.rating_bar);
        datePickerIv = findViewById(R.id.date_picker);
        timePickerIv = findViewById(R.id.time_picker);
        cinemaSpinner = findViewById(R.id.cinemaname_spinner);
        addMemoirButton = findViewById(R.id.add_button);
        addCinemaButton = findViewById(R.id.cinema_button);
        networkConnection = new NetworkConnection();
    }

    //method to retrieve the cinema data from API and attach it to the cinema spinner
    private class GetCinemaSpinnerData extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void...params){
            String result = networkConnection.getAllCinemas();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonArray.length(); i++){
                try {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    listOfCinemas.add(obj.getString("cinemaName") + "," + obj.getInt("cinemaPostcode"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return "Data has been succesfully retrieved!";
        }

            @Override
        protected void onPostExecute(String message){
            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(MemoirForm.this, android.R.layout.simple_spinner_item, listOfCinemas);
            cinemaSpinner.setAdapter(spinnerAdapter);
        }
    }

    private class AddMemoirTask extends AsyncTask<Memoir, Void, String> {
        @Override
        protected String doInBackground(Memoir... memoirs) {
            Memoir data = memoirs[0];
            networkConnection.addMemoir(data);
            return "Memoir has been successfully added!";
        }

        protected void onPostExecute(String result){
            restartToMainActivity();
            Toast.makeText(getApplicationContext(), result,
                    Toast.LENGTH_LONG).show();
        }
    }

    private class GetCinemaByNameandPostalTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            String result = networkConnection.getCinemaByNameandPostcode(params[0], Integer.parseInt(params[1]));
            Log.d("string", result);
            JSONArray array = null;
            JSONObject obj = null;
            try{
                array = new JSONArray(result);
                obj = array.getJSONObject(0);
                cinemaId = obj.getInt("cinemaId");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return "Cinema ID has been successfully retrieved!";
        }

        protected void onPostExecute(String result){

        }
    }

    private void restartToMainActivity(){
        SharedPreferences sf = getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
        Intent intent = new Intent(MemoirForm.this, MainActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt("personId", sf.getInt("personId", 0));
        bundle.putString("firstName", sf.getString("firstName", ""));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    protected void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("memoirPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        memoirId++;
        editor.putInt("memoirId", memoirId);
        editor.apply();
    }

    protected int loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences("memoirPrefs", MODE_PRIVATE);
        return sharedPreferences.getInt("memoirId", 21);
    }

}


