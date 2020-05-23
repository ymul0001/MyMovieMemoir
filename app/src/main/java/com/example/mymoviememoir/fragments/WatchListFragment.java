package com.example.mymoviememoir.fragments;

import android.app.Application;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mymoviememoir.R;
import com.example.mymoviememoir.adapter.DashboardAdapter;
import com.example.mymoviememoir.adapter.WatchlistAdapter;
import com.example.mymoviememoir.entities.WatchList;
import com.example.mymoviememoir.viewmodel.WatchlistViewModel;

import java.util.List;

public class WatchListFragment extends Fragment {
    private WatchlistViewModel watchlistViewModel;
    private TextView watchlistTitleTv;
    private RecyclerView watchlistRv;
    private List<WatchList> watchlistItems;
    private WatchlistAdapter adapter;
    private RecyclerView.Adapter itemsAdapter;
    private RecyclerView.LayoutManager layoutManager;
    public WatchListFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.watchlist_fragment, container, false);
        watchlistViewModel = new ViewModelProvider(this).get(WatchlistViewModel.class);
        watchlistViewModel.initializeVars(getActivity().getApplication());
        watchlistRv = view.findViewById(R.id.watchlist_rv);
        watchlistViewModel.getAllWatchLists().observe(this, new Observer<List<WatchList>>() {
            @Override
            public void onChanged(List<WatchList> watchLists) {
                watchlistItems = watchLists;
                adapter = new WatchlistAdapter(watchlistItems);
                layoutManager = new LinearLayoutManager(getContext());
                watchlistRv.addItemDecoration(new DividerItemDecoration(getContext(),
                        LinearLayoutManager.VERTICAL));
                watchlistRv.setAdapter(adapter);
                watchlistRv.setLayoutManager(layoutManager);
            }
        });

        return view;
    }
}
