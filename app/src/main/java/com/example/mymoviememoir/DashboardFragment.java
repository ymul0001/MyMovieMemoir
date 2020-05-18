package com.example.mymoviememoir;

import android.content.Intent;
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

import com.example.mymoviememoir.adapter.DashboardAdapter;
import com.example.mymoviememoir.model.TopfiveResult;
import com.example.mymoviememoir.networkconnection.NetworkConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;

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
        welcomeTv = view.findViewById(R.id.dashboard_welcome);
        dateTv = view.findViewById(R.id.dashboard_date);
        itemsRv = view.findViewById(R.id.topfive_list);
        networkConnection = new NetworkConnection();

        GetTopFiveMoviesTask getTopFiveMoviesTask = new GetTopFiveMoviesTask();
        getTopFiveMoviesTask.execute();
        return view;
    }

    private class GetTopFiveMoviesTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... params) {
            String result = networkConnection.getTopFiveMovies(getArguments().getInt("personId"));
            Log.i("string", result);
            return result;
        }

        @Override
        protected void onPostExecute(String jsonResult) {
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
            String firstName = getArguments().getString("firstName");
            welcomeTv.setText("Hello " + firstName + "!");
        }
    }


}
