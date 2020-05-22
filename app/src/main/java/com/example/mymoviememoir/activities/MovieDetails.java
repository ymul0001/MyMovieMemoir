package com.example.mymoviememoir.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
    private int movieId;
    private NetworkConnection networkConnection;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        networkConnection = new NetworkConnection();
        watchListButton = findViewById(R.id.watchlist_button);
        memoirButton = findViewById(R.id.memoir_button);
        Intent fromSearchFragment = getIntent();
        Bundle bundle = fromSearchFragment.getExtras();
        movieId = bundle.getInt("movieId");
        GetMovieDetailsTask getMovieDetailsTask = new GetMovieDetailsTask();
        getMovieDetailsTask.execute();
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
                movieIv = findViewById(R.id.movie_poster);
                genreTv = findViewById(R.id.genre_data);
                releaseTv = findViewById(R.id.release_data);
                countryTv = findViewById(R.id.country_data);
                directorTv = findViewById(R.id.director_data);
                synopsisTv = findViewById(R.id.synopsis_data);
                castTv = findViewById(R.id.cast_data);

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
}
