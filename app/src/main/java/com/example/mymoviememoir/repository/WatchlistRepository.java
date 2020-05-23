package com.example.mymoviememoir.repository;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.example.mymoviememoir.dao.WatchlistDao;
import com.example.mymoviememoir.database.WatchlistDatabase;
import com.example.mymoviememoir.entities.WatchList;

import java.util.List;

public class WatchlistRepository {
    private WatchlistDao watchlistDao;
    private LiveData<List<WatchList>> allwatchLists;
    private WatchList watchList;

    public WatchlistRepository(Application application){
        //instantiate database
        WatchlistDatabase db = WatchlistDatabase.getInstance(application);

        //call the watchlistdao abstract methods
        watchlistDao = db.watchlistDao();
    }

    public void insert(final WatchList watchlist) {
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                watchlistDao.insert(watchlist);
            }
        });
    }

    public void delete(final WatchList watchList){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                watchlistDao.insert(watchList);
            }
        });
    }

    public LiveData<List<WatchList>> getAllWatchlists(){
        //get all watchlists data from the room database with dao
        allwatchLists = watchlistDao.getAll();
        return allwatchLists;
    }

    public WatchList getWatchListById(final int id){
        //get watchlist data from the room database with dao by id
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                watchList = watchlistDao.findByMovieId(id);
            }
        });
        return watchList;
    }
}
