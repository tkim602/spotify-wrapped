package com.example.spotifywrapped;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.spotifywrapped.Entities.Account;

import java.util.List;

public class SpotifyWrappedViewModel extends AndroidViewModel {
    private Repository myRepository;

    private LiveData<List<Account>> allAccounts;

    public SpotifyWrappedViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = new Repository(application);
    }

    public LiveData<List<Account>> getAllAccounts() {
        allAccounts = myRepository.getAllAccounts();
        return allAccounts;
    }

    public void addNewAccount(Account account) {
        myRepository.addAccount(account);
    }
    public void updateAccount(Account account) {myRepository.updateAccount(account);}

}
