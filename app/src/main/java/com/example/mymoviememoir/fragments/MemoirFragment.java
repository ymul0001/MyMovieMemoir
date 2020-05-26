package com.example.mymoviememoir.fragments;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.adapter.MemoirAdapter;
import com.example.mymoviememoir.entities.FullMemoir;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MemoirFragment extends Fragment {

    private RecyclerView memoirRv;
    private MemoirAdapter.ClickListener listener;
    private List<FullMemoir> memoirItems;
    private MemoirAdapter adapter;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    NetworkConnection networkConnection;
    public MemoirFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.memoir_fragment, container, false);
        networkConnection = new NetworkConnection();
        //init views and all variables
        memoirItems = new ArrayList<>();
        initView(view);
        //execute the fetch data method
        GetAllMemoirTasks getAllMemoirTasks = new GetAllMemoirTasks();
        getAllMemoirTasks.execute();

        return view;
    }

    public void initView(View view){
        memoirRv = view.findViewById(R.id.memoir_items);
        layoutManager = new LinearLayoutManager(getContext());
    }

    private class GetAllMemoirTasks extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            String result = networkConnection.getAllMemoirs();
            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = null;
                JSONObject cinemaObj = null;
                try {
                    obj = array.getJSONObject(i);
                    cinemaObj = obj.getJSONObject("cinemaId");
                    String movieResult = networkConnection.getMovieDetailsByNameAndReleaseYear(obj.getString("movieName"), Integer.parseInt(obj.get("movieReleasedate").toString().substring(0,4)));
                    Log.d("string", movieResult);
                    Log.d("string", cinemaObj.toString());
                    String posterPath = new JSONObject(movieResult).getJSONArray("results").getJSONObject(0).getString("poster_path");
                    Float publicRating = Float.parseFloat(new JSONObject(movieResult).getJSONArray("results").getJSONObject(0).get("vote_average").toString());
                    memoirItems.add(new FullMemoir(obj.getString("movieName"), posterPath, sdf.format(input.parse(obj.get("movieReleasedate").toString())), sdf.format(input.parse(obj.get("movieWatchdate").toString())), cinemaObj.getInt("cinemaPostcode"), Float.parseFloat(obj.get("movieRating").toString()), publicRating,obj.getString("movieComment")));
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            return "Memoir has been retrieved!";
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            adapter = new MemoirAdapter(memoirItems, listener);
            memoirRv.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
            memoirRv.setAdapter(adapter);
            memoirRv.setLayoutManager(layoutManager);
        }
    }
}
