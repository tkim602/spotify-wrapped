package com.example.spotifywrapped;

import android.app.Application;
import android.os.Looper;

import com.example.spotifywrapped.DAOs.AccountDAO;
import com.example.spotifywrapped.Entities.Account;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import android.os.Handler;

import androidx.lifecycle.LiveData;

public class Repository {
    private final AccountDAO accountDAO;

    ExecutorService executor;

    Handler handler;

    public Repository(Application application) {
        SpotifyWrappedDatabase spotifyWrappedDatabase = SpotifyWrappedDatabase.getInstance(application);
        this.accountDAO = spotifyWrappedDatabase.getAccountDAO();

        executor = Executors.newSingleThreadExecutor();
        handler = new Handler(Looper.getMainLooper());
    }

    public void addAccount(Account account) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                accountDAO.insert(account);
            }
        });
    }

    public void removeAccount(Account account) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                accountDAO.delete(account);
            }
        });
    }



    public LiveData<List<Account>> getAllAccounts() {
        return accountDAO.getAllAssignments();
    }
}
