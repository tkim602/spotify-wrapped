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

    @ColumnInfo(name = "account_name")
    private String accountName;

    public Account(String accountEmail, String accountPassword, String accountName) {
        this.accountEmail = accountEmail;
        this.accountPassword = accountPassword;
        this.accountName = accountName;
    }
    public  Account() {

    }

    public int getAccountID() {
        return accountID;
    }

    public void setAccountID(int accountID) {
        this.accountID = accountID;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
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
        return "Account ID: " + accountID + ", Email: " + accountEmail + ", Password: " + accountPassword;
    }
}