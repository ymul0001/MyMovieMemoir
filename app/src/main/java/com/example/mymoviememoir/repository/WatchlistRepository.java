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
    private LiveData<WatchList> watchList;
    private int movieCount;

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
                watchlistDao.delete(watchList);
            }
        });
    }

    public LiveData<List<WatchList>> getAllWatchlists(){
        //get all watchlists data from the room database with dao
        allwatchLists = watchlistDao.getAll();
        return allwatchLists;
    }

    public int getMovieCount(final int movieId){
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                movieCount = watchlistDao.findTotalMovieData(movieId);
            }
        });
        return movieCount;
    }

    public LiveData<WatchList> getWatchListById(final int movieId){
        //get watchlist data from the room database with dao by id
        WatchlistDatabase.databaseWriteExecutor.execute(new Runnable() {
            @Override
            public void run() {
                LiveData<WatchList> runWatchlist = watchlistDao.findByMovieId(movieId);
                setWatchList(runWatchlist);
            }
        });
        return watchList;
    }

    public void setWatchList(LiveData<WatchList> watchList){
        this.watchList=watchList;
    }

}
