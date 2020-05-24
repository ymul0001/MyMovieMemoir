package com.example.mymoviememoir.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.mymoviememoir.entities.WatchList;
import com.example.mymoviememoir.repository.WatchlistRepository;

import java.util.List;

public class WatchlistViewModel extends ViewModel {
    private WatchlistRepository repository;
    private MutableLiveData<List<WatchList>> allWatchlists;
    private int movieCount;

    public WatchlistViewModel(){
        allWatchlists = new MutableLiveData<>();

    }

    public void initializeVars(Application application){
        repository = new WatchlistRepository(application);
    }

    public LiveData<List<WatchList>> getAllWatchLists(){
        return repository.getAllWatchlists();
    }

    public LiveData<WatchList> getWatchListById(int movieId){
        return repository.getWatchListById(movieId);
    }

    public int getMovieCount(int movieId){
        return repository.getMovieCount(movieId);
    }

    public void insert(WatchList watchList){
        repository.insert(watchList);
    }

    public void delete(WatchList watchList){
        repository.delete(watchList);
    }
}
