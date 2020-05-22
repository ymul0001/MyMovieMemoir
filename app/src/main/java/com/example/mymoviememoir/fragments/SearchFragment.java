package com.example.mymoviememoir.fragments;

import android.content.Intent;
import android.graphics.Movie;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.activities.MovieDetails;
import com.example.mymoviememoir.adapter.DashboardAdapter;
import com.example.mymoviememoir.adapter.SearchAdapter;
import com.example.mymoviememoir.model.MovieResult;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchFragment extends Fragment {
    private EditText searchEt;
    private Button searchButton;
    private RecyclerView movieRv;
    private SearchAdapter.RecyclerViewClickListener listener;
    NetworkConnection networkConnection = null;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SearchAdapter adapter;

    public SearchFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.search_fragment, container, false);
        searchEt = view.findViewById(R.id.search_bar);
        searchButton = view.findViewById(R.id.search_button);
        movieRv = view.findViewById(R.id.search_rv);
        networkConnection = new NetworkConnection();

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetMoviesFromApiTask getMoviesFromApiTask = new GetMoviesFromApiTask();
                getMoviesFromApiTask.execute(new String[]{searchEt.getText().toString()});
            }
        });
        return view;
    }

    private class GetMoviesFromApiTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            JSONObject fullResult = null;
            String result = "";
            try {
                fullResult = new JSONObject(networkConnection.getMoviesFromApi(params[0]));
                result = fullResult.getJSONArray("results").toString();

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            ArrayList<MovieResult> movieResults = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(jsonResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if(jsonArray.length() != 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = null;
                    try {
                        obj = jsonArray.getJSONObject(i);
                        if (!obj.getString("release_date").equals("")) {
                            MovieResult item = new MovieResult(obj.getString("poster_path"), obj.getString("title"), obj.getString("release_date"));
                            movieResults.add(item);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            else{
                Toast.makeText(getContext(), "No movies found! please enter another keywords", Toast.LENGTH_LONG).show();
            }
            setOnClickListener();
            adapter = new SearchAdapter(movieResults, listener);
            layoutManager = new LinearLayoutManager(getContext());
            movieRv.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
            movieRv.setAdapter(adapter);
            movieRv.setLayoutManager(layoutManager);
        }
}

    private void setOnClickListener() {
        listener = new SearchAdapter.RecyclerViewClickListener() {
            @Override
            public void onClick(View v, int position) {
                Intent toMovieDetails = new Intent(getContext(), MovieDetails.class);
                startActivity(toMovieDetails);
            }
        };
    }
}
