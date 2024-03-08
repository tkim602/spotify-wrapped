package com.example.spotifywrapped;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.spotifywrapped.DAOs.AccountDAO;
import com.example.spotifywrapped.Entities.Account;

@Database(entities = {Account.class},version = 1)
public abstract class SpotifyWrappedDatabase extends RoomDatabase {
    public abstract AccountDAO getAccountDAO();
    private static SpotifyWrappedDatabase dbInstance;

    public static synchronized SpotifyWrappedDatabase getInstance(Context context) {
        if(dbInstance == null) {
            dbInstance = Room.databaseBuilder(context.getApplicationContext(), SpotifyWrappedDatabase.class, "spotify_wrapped_db").fallbackToDestructiveMigration().build();
        }
        return dbInstance;
    }
}

