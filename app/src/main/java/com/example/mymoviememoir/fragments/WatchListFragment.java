package com.example.mymoviememoir.fragments;

import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.mymoviememoir.R;
import com.example.mymoviememoir.activities.MovieDetails;
import com.example.mymoviememoir.adapter.WatchlistAdapter;
import com.example.mymoviememoir.entities.WatchList;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;

import java.util.List;

import static android.content.Context.MODE_PRIVATE;

public class WatchListFragment extends Fragment {
    private WatchlistViewModel watchlistViewModel;
    private TextView watchlistTitleTv;
    private RecyclerView watchlistRv;
    private WatchlistAdapter.ClickListeners listeners;
    private List<WatchList> watchlistItems;
    private WatchlistAdapter adapter;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    private SharedPreferences sf;
    public WatchListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getActivity().getApplication());
        watchlistRv = view.findViewById(R.id.watchlist_rv);
        sf = getActivity().getSharedPreferences("shared_prefss", MODE_PRIVATE);
        watchlistViewModel.getAllWatchLists().observe(this, new Observer<List<WatchList>>() {
            @Override
            public void onChanged(List<WatchList> watchLists) {
                watchlistItems = watchLists;
                setOnClickListener(watchlistItems);
                adapter = new WatchlistAdapter(watchlistItems, listeners);
                layoutManager = new LinearLayoutManager(getContext());
                watchlistRv.addItemDecoration(new DividerItemDecoration(getContext(),
                        LinearLayoutManager.VERTICAL));
                watchlistRv.setAdapter(adapter);
                watchlistRv.setLayoutManager(layoutManager);
            }
        });
        return view;
    }

    private void setOnClickListener(final List<WatchList> watchlistItems) {
        listeners = new WatchlistAdapter.ClickListeners() {
            @Override
            public void onDeleteButtonClick(int position) {
                showAlertDialog(getContext(), watchlistViewModel, position);
            }

            @Override
            public void onViewButtonClick(int position) {
                Intent toMovieDetails = new Intent(getContext(), MovieDetails.class);
                Bundle bundle = new Bundle();
                bundle.putInt("movieId", watchlistItems.get(position).getMovieId());
                toMovieDetails.putExtras(bundle);
                startActivity(toMovieDetails);
            }
        };
    }

    private void showAlertDialog(Context context, final WatchlistViewModel watchlistViewModel, final int position){
        new AlertDialog.Builder(context)
                .setTitle("Confirm deletion")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        watchlistViewModel.delete(watchlistItems.get(position));
                        sf.edit().remove("movieId").commit();
                    }
                })

                // dismissing the dialog by specifying null for the listener argument
                .setNegativeButton(android.R.string.no, null)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }
}
