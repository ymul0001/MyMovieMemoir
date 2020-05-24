package com.example.mymoviememoir.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.mymoviememoir.dao.WatchlistDao;
import com.example.mymoviememoir.entities.WatchList;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = WatchList.class, version = 2, exportSchema = false)
public abstract class WatchlistDatabase extends RoomDatabase {

    //make the database singleton, so everywhere in the app will only use one type of WatchlistDatabase
    private static WatchlistDatabase instance;

    //call the data access object
    public abstract WatchlistDao watchlistDao();

    //creating an ExecutorService with fixed number of threads
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    //make the database, synchronized so that multi threads do not accidentally create more instances
    public static synchronized WatchlistDatabase getInstance(final Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(), WatchlistDatabase.class, "WatchlistDatabase")
            .fallbackToDestructiveMigration() //code to prevent failure in versioning instances
                    .build();
        }
        return instance;
    }
}
