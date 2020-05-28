package com.example.mymoviememoir.activities;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MemoirDetails extends AppCompatActivity {
    private ImageView memoirPosterIv;
    private TextView memoirTitleTv;
    private TextView memoirGenreTv;
    private TextView memoirCountryTv;
    private TextView memoirReleaseTv;
    private TextView memoirDirectorTv;
    private TextView memoirCastTv;
    private TextView memoirSynopsisTv;
    private int movieId;
    NetworkConnection networkConnection;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.memoir_detail);
        initView();

        //get bundle from intent
        Intent fromMemoirFragment = getIntent();
        Bundle bundle = fromMemoirFragment.getExtras();
        movieId = bundle.getInt("movieId");

        //initiate the MovieAPI fetch task
        GetMovieDetailsTask getMovieDetailsTask = new GetMovieDetailsTask();
        getMovieDetailsTask.execute();
    }


    private void initView(){
        memoirPosterIv = findViewById(R.id.memoir_detail_poster);
        memoirTitleTv = findViewById(R.id.movie_title);
        memoirGenreTv = findViewById(R.id.memoir_genre_data);
        memoirCountryTv = findViewById(R.id.memoir_country_data);
        memoirReleaseTv = findViewById(R.id.memoir_release_data);
        memoirDirectorTv = findViewById(R.id.memoir_director_data);
        memoirCastTv = findViewById(R.id.memoir_cast_data);
        memoirSynopsisTv = findViewById(R.id.memoir_synopsis_data);
        networkConnection = new NetworkConnection();
    }

    //method for getting movie details from the movieDB by movie id
    private class GetMovieDetailsTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = networkConnection.getMovieDetails(movieId);
            return result;
        }

        //on post will apply the details into each view
        @Override
        protected void onPostExecute(String jsonResult) {
            JSONObject obj = null;
            String genresStr ="";
            String directorsStr = "";
            String topSixCastStr = "";
            try {
                obj = new JSONObject(jsonResult);
                memoirTitleTv.setText(obj.getString("title"));
                JSONArray genres = obj.getJSONArray("genres");
                JSONArray productionCountries = obj.getJSONArray("production_countries");
                JSONObject credits = obj.getJSONObject("credits");
                JSONArray cast = credits.getJSONArray("cast");
                JSONArray crews = credits.getJSONArray("crew");
                for(int i = 0; i < genres.length(); i++)
                {
                    JSONObject genre = genres.getJSONObject(i);
                    genresStr = genresStr + genre.getString("name") + "; ";
                }
                for(int i = 0; i < 6; i++)
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
                String backdropPath = obj.getString("backdrop_path");
                memoirGenreTv.setText(genresStr);
                memoirReleaseTv.setText(obj.getString("release_date"));
                memoirCastTv.setText(topSixCastStr);
                memoirCountryTv.setText(productionCountries.getJSONObject(0).getString("name"));
                Picasso.get().load("https://image.tmdb.org/t/p/original" + backdropPath).into(memoirPosterIv);
                memoirDirectorTv.setText(directorsStr);
                memoirSynopsisTv.setText(obj.getString("overview"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}