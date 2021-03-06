package com.example.mymoviememoir.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;


import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.WatchList;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
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
    private RatingBar ratingBar;
    private WatchlistViewModel watchlistViewModel;
    private int movieId;
    private String backdropPath;
    private NetworkConnection networkConnection;
    CallbackManager callbackManager;
    ShareDialog shareDialog;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_details);
        initView();
        networkConnection = new NetworkConnection();
        callbackManager = CallbackManager.Factory.create();
        shareDialog = new ShareDialog(this);

        //initialize the ViewModel
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getApplication());

        //get movie ids for getting movie details
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        movieId = bundle.getInt("movieId");
        if (bundle.get("alpha") != null)
        {
            watchListButton.setEnabled(false);
            watchListButton.setAlpha(.5f);
        }
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
                    new AlertDialog.Builder(MovieDetails.this)
                            .setTitle("Post to Facebook")
                            .setMessage("Do you also want to notify your friends on facebook?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    ShareLinkContent linkContent = new ShareLinkContent.Builder()
                                            .setQuote("I found a good movie called " + title + ". You can find it easily at:")
                                            .setContentUrl(Uri.parse("https://www.netflix.com/id-en/"))
                                            .build();
                                    if (shareDialog.canShow(ShareLinkContent.class))
                                    {
                                        shareDialog.show(linkContent);
                                    }
                                    shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
                                        @Override
                                        public void onSuccess(Sharer.Result result) {
                                            Toast.makeText(getApplicationContext(), "Successfully post a message!",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onCancel() {
                                            Toast.makeText(getApplicationContext(), "Cancelling the message posting...",
                                                    Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onError(FacebookException error) {
                                            Toast.makeText(getApplicationContext(), "There is something wrong with the system, try again later...",
                                                    Toast.LENGTH_LONG).show();
                                        }
                                    });
                                    dialog.cancel();
                                }
                            })
                            // dismissing the dialog by specifying null for the listener argument
                            .setNegativeButton(android.R.string.no, null)
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    WatchList newWatchList = new WatchList(id, title, releaseDate, addedDate, addedTime);
                    watchlistViewModel.insert(newWatchList);
                    Toast.makeText(getApplicationContext(), "Successfully added the movie to watchlist!",
                            Toast.LENGTH_LONG).show();
                    /*saveData();*/
                }
            }
        });

        memoirButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MovieDetails.this, MemoirForm.class);
                Bundle bundle = new Bundle();
                bundle.putString("movieTitle", titleTv.getText().toString());
                bundle.putString("releaseDate", releaseTv.getText().toString());
                bundle.putString("backdropPath", backdropPath);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

    }

    public void initView(){
        ratingBar = findViewById(R.id.movie_rating);
        movieIv = findViewById(R.id.memoir_detail_poster);
        genreTv = findViewById(R.id.memoir_genre_data);
        releaseTv = findViewById(R.id.memoir_release_data);
        countryTv = findViewById(R.id.memoir_country_data);
        directorTv = findViewById(R.id.memoir_director_data);
        synopsisTv = findViewById(R.id.memoir_synopsis_data);
        watchListButton = findViewById(R.id.watchlist_button);
        memoirButton = findViewById(R.id.memoir_button);
        castTv = findViewById(R.id.memoir_cast_data);
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
                ratingBar.setRating(Math.round(Float.parseFloat(obj.get("vote_average").toString()))/2);
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
                backdropPath = obj.getString("backdrop_path");
                genreTv.setText(genresStr);
                releaseTv.setText(obj.getString("release_date"));
                castTv.setText(topSixCastStr);
                countryTv.setText(production_countries.getJSONObject(0).getString("name"));
                Picasso.get().load("https://image.tmdb.org/t/p/original" + backdropPath).into(movieIv);
                directorTv.setText(directorsStr);
                synopsisTv.setText(obj.getString("overview"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
