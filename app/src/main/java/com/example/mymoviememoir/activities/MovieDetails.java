package com.example.mymoviememoir.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.WatchList;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieDetails extends AppCompatActivity {
    private ImageView movieIv;
    private TextView titleTv;
    private TextView genreTv;
    private TextView releaseTv;
    private TextView countryTv;
    private TextView directorTv;
    private TextView synopsisTv;
    private TextView castTv;
    private Button watchListButton;
    private Button memoirButton;
    private WatchlistViewModel watchlistViewModel;
    private int movieId;
    private NetworkConnection networkConnection;
    /*private List<WatchList> watchLists;*/

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        movieIv = findViewById(R.id.movie_poster);
        genreTv = findViewById(R.id.genre_data);
        releaseTv = findViewById(R.id.release_data);
        countryTv = findViewById(R.id.country_data);
        directorTv = findViewById(R.id.director_data);
        synopsisTv = findViewById(R.id.synopsis_data);
        watchListButton = findViewById(R.id.watchlist_button);
        memoirButton = findViewById(R.id.memoir_button);
        castTv = findViewById(R.id.cast_data);
        networkConnection = new NetworkConnection();

        //initialize the ViewModel
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getApplication());

        //get movie ids for getting movie details
        Intent fromSearchFragment = getIntent();
        Bundle bundle = fromSearchFragment.getExtras();
        movieId = bundle.getInt("movieId");
        SharedPreferences sf = getSharedPreferences("shared_prefss", MODE_PRIVATE);
        sf.getInt("movieId", 0);
        if (movieId == sf.getInt("movieId", 0)) {
            watchListButton.setEnabled(false);
            watchListButton.setAlpha(.5f);
        }
        //get movie details
        GetMovieDetailsTask getMovieDetailsTask = new GetMovieDetailsTask();
        getMovieDetailsTask.execute();




        watchListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int id = movieId;
                final String title = titleTv.getText().toString();
                final String releaseDate = releaseTv.getText().toString();
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                Date date = new Date();
                final String addedDate = dateFormat.format(date);
                final String addedTime = timeFormat.format(date);
                if (watchlistViewModel.getWatchListById(movieId) != null)
                {
                    Toast.makeText(getApplicationContext(), "Duplicated movies!",
                            Toast.LENGTH_LONG).show();
                }
                else{
                    WatchList newWatchList = new WatchList(id, title, releaseDate, addedDate, addedTime);
                    watchlistViewModel.insert(newWatchList);
                    Toast.makeText(getApplicationContext(), "Successfully added the movie to watchlist!",
                            Toast.LENGTH_LONG).show();
                    saveData();
                }
            }
        });
    }

    private class GetMovieDetailsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = networkConnection.getMovieDetails(movieId);
            return result;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            JSONObject obj = null;
            titleTv = findViewById(R.id.movie_title);
            String genresStr ="";
            String directorsStr = "";
            String topSixCastStr = "";
            try {
                obj = new JSONObject(jsonResult);
                titleTv.setText(obj.getString("title"));
                JSONArray genres = obj.getJSONArray("genres");
                JSONArray production_countries = obj.getJSONArray("production_countries");
                JSONObject credits = obj.getJSONObject("credits");
                JSONArray cast = credits.getJSONArray("cast");
                JSONArray crews = credits.getJSONArray("crew");
                for(int i = 0; i < genres.length(); i++)
                {
                    JSONObject genre = genres.getJSONObject(i);
                    genresStr = genresStr + genre.getString("name") + "; ";
                }
                for(int i = 0; i< 6; i++)
                {
                    JSONObject actor = cast.getJSONObject(i);
                    topSixCastStr = topSixCastStr + actor.getString("name") + "; ";
                }
                for(int i = 0; i < crews.length(); i++)
                {
                    JSONObject crew = crews.getJSONObject(i);
                    String job = crew.getString("job");
                    if (job.equals("Director"))
                    {
                        directorsStr = directorsStr + crew.getString("name") + "; ";
                    }
                }
                genreTv.setText(genresStr);
                releaseTv.setText(obj.getString("release_date"));
                castTv.setText(topSixCastStr);
                countryTv.setText(production_countries.getJSONObject(0).getString("name"));
                Picasso.get().load("https://image.tmdb.org/t/p/original" + obj.getString("backdrop_path")).into(movieIv);
                directorTv.setText(directorsStr);
                synopsisTv.setText(obj.getString("overview"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void saveData(){
        SharedPreferences sharedPreferences = getSharedPreferences("shared_prefss", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("movieId", movieId);
        editor.apply();
    }
}
