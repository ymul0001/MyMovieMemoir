package com.example.mymoviememoir.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.adapter.DashboardAdapter;
import com.example.mymoviememoir.model.TopfiveResult;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;


public class DashboardFragment extends Fragment {
    private TextView welcomeTv;
    private TextView dateTv;
    private RecyclerView itemsRv;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private DashboardAdapter adapter;
    private final String SHARED_PREF = "sharedPrefs";
    private JSONArray jsonArray = null;
    NetworkConnection networkConnection;
    public DashboardFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dashboard_fragment, container, false);

        //views are initialised in here because those need view objects
        welcomeTv = view.findViewById(R.id.dashboard_welcome);
        dateTv = view.findViewById(R.id.dashboard_date);
        itemsRv = view.findViewById(R.id.watchlist_rv);
        networkConnection = new NetworkConnection();
        SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd/MM/yyyy");
        Date sysdate = new Date();
        dateTv.setText(sdf.format(sysdate));
        GetTopFiveMoviesTask getTopFiveMoviesTask = new GetTopFiveMoviesTask();
        getTopFiveMoviesTask.execute();
        return view;
    }

    //method for getting top five movies from the database
    private class GetTopFiveMoviesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            SharedPreferences sf = getContext().getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
            String result = networkConnection.getTopFiveMovies(sf.getInt("personId", 0));
            return result;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
            SharedPreferences sf = getContext().getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
            ArrayList<TopfiveResult> topfiveResults = new ArrayList<>();
            try {
                jsonArray = new JSONArray(jsonResult);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < jsonArray.length(); i++)
            {
                JSONObject obj = null;
                try {
                    obj = jsonArray.getJSONObject(i);
                    TopfiveResult item = new TopfiveResult(obj.get("name").toString(), obj.get("ratingScore").toString(), obj.get("date").toString());
                    topfiveResults.add(item);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            adapter = new DashboardAdapter(topfiveResults);
            layoutManager = new LinearLayoutManager(getContext());
            itemsRv.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
            itemsRv.setAdapter(adapter);
            itemsRv.setLayoutManager(layoutManager);
            String firstName = sf.getString("firstName", "No name!");
            welcomeTv.setText("Hello " + firstName + "!");
        }
    }
}
