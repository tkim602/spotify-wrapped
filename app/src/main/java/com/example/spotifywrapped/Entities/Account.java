package com.example.spotifywrapped.Entities;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "accounts_table")
public class Account {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "account_id")
    private int accountID;

    @ColumnInfo(name = "account_email")
    private String accountEmail;

    @ColumnInfo(name = "account_password")
    private String accountPassword;

    @ColumnInfo(name = "account_token")
    private String accountToken;


    public Account(String accountEmail, String accountPassword) {
        this.accountEmail = accountEmail;
        this.accountPassword = accountPassword;
        this.accountToken = null;
    }
    public  Account() {

    }

    public String getAccountToken() {
        return accountToken;
    }

    public void setAccountToken(String accountToken) {
        this.accountToken = accountToken;
    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountEmail() {
        return accountEmail;
    }

    public void setAccountEmail(String accountEmail) {
        this.accountEmail = accountEmail;
    }

    public String getAccountPassword() {
        return accountPassword;
    }

    public void setAccountPassword(String accountPassword) {
        this.accountPassword = accountPassword;
    }
    @NonNull
    @Override
    public String toString() {
        return "Account ID: " + accountID + ", Email: " + accountEmail + ", Password: " + accountPassword + ", Token: " + accountToken;
    }
}