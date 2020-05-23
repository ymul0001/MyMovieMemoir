package com.example.mymoviememoir.fragments;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.entities.WatchList;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;

import java.util.List;

public class WatchListFragment extends Fragment {
    private WatchlistViewModel watchlistViewModel;
    public WatchListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getActivity().getApplication());
        watchlistViewModel.getAllWatchLists().observe(this, new Observer<List<WatchList>>() {
            @Override
            public void onChanged(List<WatchList> watchLists) {
                Toast.makeText(getContext(), "Hello to watchlist fragment!",
                        Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
