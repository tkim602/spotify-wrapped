package com.example.spotifywrapped.DAOs;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.spotifywrapped.Entities.Account;

import java.util.List;

@Dao
public interface AccountDAO {
    @Insert
    void insert(Account account);

    @Delete
    void delete(Account account);

    @Query("SELECT * FROM  accounts_table")
    LiveData<List<Account>> getAllAssignments();
}