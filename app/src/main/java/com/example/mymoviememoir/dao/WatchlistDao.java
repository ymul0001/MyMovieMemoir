package com.example.mymoviememoir.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.mymoviememoir.entities.WatchList;

import java.util.List;

@Dao
public interface WatchlistDao {

    @Query("SELECT * FROM watchlist")
    LiveData<List<WatchList>> getAll();

    @Query("SELECT * FROM watchlist WHERE movieId = :movieId LIMIT 1")
    LiveData<WatchList> findByMovieId(int movieId);

    @Query("SELECT COUNT(*) FROM watchlist WHERE movieId = :movieId")
    int findTotalMovieData(int movieId);

    @Insert
    void insert(WatchList watchList);

    @Delete
    void delete(WatchList watchList);
}
