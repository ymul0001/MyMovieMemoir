package com.example.mymoviememoir.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.activities.MemoirDetails;
import com.example.mymoviememoir.adapter.MemoirAdapter;
import com.example.mymoviememoir.entities.FullMemoir;
import com.example.mymoviememoir.networkconnection.NetworkConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class MemoirFragment extends Fragment {
    private RecyclerView memoirRv;
    private MemoirAdapter.ClickListener listener;
    private List<FullMemoir> memoirItems;
    private List<FullMemoir> memoirItemsByGenre;
    private MemoirAdapter adapter;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private Spinner sortSpinner;
    private Spinner filterSpinner;
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
        SharedPreferences sf = getActivity().getSharedPreferences("dashboardPreferences", MODE_PRIVATE);
        //execute the fetch data method
        GetAllMemoirTasks getAllMemoirTasks = new GetAllMemoirTasks();
        getAllMemoirTasks.execute(sf.getInt("personId", 0));

        //listeners start here
        sortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String sortCategory = parent.getItemAtPosition(position).toString();
                final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                List<FullMemoir> processedItems = new ArrayList<>();
                if(memoirItemsByGenre == null){
                    processedItems = memoirItems;
                    setOnClickListener(processedItems);
                }
                else if(memoirItemsByGenre != null) {
                    processedItems = memoirItemsByGenre;
                    setOnClickListener(processedItems);
                }
                switch(sortCategory){
                    case "release date - asc":
                        Collections.sort(processedItems, new Comparator<FullMemoir>() {
                            @Override
                            public int compare(FullMemoir o1, FullMemoir o2) {
                                Date dateOne = new Date();
                                Date dateTwo = new Date();
                                try {
                                    dateOne = sdf.parse(o1.getReleaseDate());
                                    dateTwo = sdf.parse(o2.getReleaseDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return dateOne.compareTo(dateTwo);
                            }
                        });
                        break;
                    case "release date - desc":
                        Collections.sort(processedItems, new Comparator<FullMemoir>() {
                            @Override
                            public int compare(FullMemoir o1, FullMemoir o2) {
                                Date dateOne = new Date();
                                Date dateTwo = new Date();
                                try {
                                    dateOne = sdf.parse(o1.getReleaseDate());
                                    dateTwo = sdf.parse(o2.getReleaseDate());
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                                return dateTwo.compareTo(dateOne);
                            }
                        });
                        break;
                    case "user rating - asc":
                        Collections.sort(processedItems, new Comparator<FullMemoir>() {
                            @Override
                            public int compare(FullMemoir o1, FullMemoir o2) {
                                return Float.compare(o1.getUserRating(), o2.getUserRating());
                            }
                        });
                        break;
                    case "user rating - desc":
                        Collections.sort(processedItems, new Comparator<FullMemoir>() {
                            @Override
                            public int compare(FullMemoir o1, FullMemoir o2) {
                                return Float.compare(o2.getUserRating(), o1.getUserRating());
                            }
                        });
                        break;
                    case "public rating - asc":
                        Collections.sort(processedItems, new Comparator<FullMemoir>() {
                            @Override
                            public int compare(FullMemoir o1, FullMemoir o2) {
                                return Float.compare(o1.getPublicRating(), o2.getPublicRating());
                            }
                        });
                        break;
                    case "public rating - desc":
                        Collections.sort(processedItems, new Comparator<FullMemoir>() {
                            @Override
                            public int compare(FullMemoir o1, FullMemoir o2) {
                                return Float.compare(o2.getPublicRating(), o1.getPublicRating());
                            }
                        });
                        break;
                    default:
                        break;
            }
                adapter = new MemoirAdapter(processedItems, listener);
                memoirRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        filterSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String genreName = parent.getItemAtPosition(position).toString();
                memoirItemsByGenre = new ArrayList<>();
                int genreId = 0;
                switch(genreName){
                    case "None":
                        adapter = new MemoirAdapter(memoirItems, listener);
                        memoirItemsByGenre = null; break;
                    case "Action":
                        genreId = 28;
                        setOnClickListener(memoirItemsByGenre);
                        adapter = new MemoirAdapter(memoirItemsByGenre, listener); break;
                    case "Comedy":
                        genreId = 35;
                        setOnClickListener(memoirItemsByGenre);
                        adapter = new MemoirAdapter(memoirItemsByGenre, listener); break;
                    case "Fantasy":
                        genreId = 14;
                        setOnClickListener(memoirItemsByGenre);
                        adapter = new MemoirAdapter(memoirItemsByGenre, listener); break;
                    case "Romance":
                        genreId = 10479;
                        setOnClickListener(memoirItemsByGenre);
                        adapter = new MemoirAdapter(memoirItemsByGenre, listener); break;
                    case "Thriller":
                        genreId = 53;
                        setOnClickListener(memoirItemsByGenre);
                        adapter = new MemoirAdapter(memoirItemsByGenre, listener); break;
                    default:
                        genreId = 0;
                        setOnClickListener(memoirItemsByGenre);
                        adapter = new MemoirAdapter(memoirItemsByGenre, listener); break;
                }
                for(FullMemoir item: memoirItems){
                    ArrayList<Integer> genres = item.getGenreIds();
                    if(genres.indexOf(genreId) != -1){
                        memoirItemsByGenre.add(item);
                    }
                }
                memoirRv.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }

    private void setOnClickListener(final List<FullMemoir> memoirItems) {
        listener = new MemoirAdapter.ClickListener() {
            @Override
            public void onViewButtonClick(int position) {
                Intent intent = new Intent(getContext(), MemoirDetails.class);
                Bundle bundle = new Bundle();
                bundle.putInt("movieId", memoirItems.get(position).getMovieId());
                intent.putExtras(bundle);
                startActivity(intent);
            }

        };
    }

    public void initView(View view){
        memoirRv = view.findViewById(R.id.memoir_items);
        layoutManager = new LinearLayoutManager(getContext());
        sortSpinner = view.findViewById(R.id.sort_spinner);
        filterSpinner = view.findViewById(R.id.filter_spinner);
    }

    private class GetAllMemoirTasks extends AsyncTask<Integer, Void, String> {
        @Override
        protected String doInBackground(Integer... params) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat input = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            String result = networkConnection.getAllMemoirsByPersonId(params[0]);
            JSONArray array = null;
            try {
                array = new JSONArray(result);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            for(int i = 0; i < array.length(); i++) {
                JSONObject obj = null;
                JSONObject cinemaObj = null;
                ArrayList<Integer> genreId = new ArrayList<>();
                try {
                    obj = array.getJSONObject(i);
                    cinemaObj = obj.getJSONObject("cinemaId");
                    String movieResult = networkConnection.getMovieDetailsByNameAndReleaseYear(obj.getString("movieName"), Integer.parseInt(obj.get("movieReleasedate").toString().substring(0,4)));
                    Log.d("string", movieResult);
                    Log.d("string", cinemaObj.toString());
                    String posterPath = new JSONObject(movieResult).getJSONArray("results").getJSONObject(0).getString("poster_path");
                    Float publicRating = Float.parseFloat(new JSONObject(movieResult).getJSONArray("results").getJSONObject(0).get("vote_average").toString());
                    JSONArray genres = new JSONObject(movieResult).getJSONArray("results").getJSONObject(0).getJSONArray("genre_ids");
                    for(int j = 0; j < genres.length(); j++){
                        genreId.add(genres.getInt(j));
                    }
                    memoirItems.add(new FullMemoir(obj.getString("movieName"), posterPath, sdf.format(input.parse(obj.get("movieReleasedate").toString())), sdf.format(input.parse(obj.get("movieWatchdate").toString())), cinemaObj.getInt("cinemaPostcode"), Float.parseFloat(obj.get("movieRating").toString()), publicRating,obj.getString("movieComment"), genreId, new JSONObject(movieResult).getJSONArray("results").getJSONObject(0).getInt("id")));
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
            setOnClickListener(memoirItems);
            adapter = new MemoirAdapter(memoirItems, listener);
            memoirRv.addItemDecoration(new DividerItemDecoration(getContext(),
                    LinearLayoutManager.VERTICAL));
            memoirRv.setAdapter(adapter);
            memoirRv.setLayoutManager(layoutManager);
        }
    }
}
