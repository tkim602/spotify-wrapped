package com.example.spotifywrapped.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.spotifywrapped.Entities.Account;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.SpotifyWrappedDatabase;
import com.example.spotifywrapped.SpotifyWrappedViewModel;
import com.spotify.sdk.android.auth.AuthorizationClient;
import com.spotify.sdk.android.auth.AuthorizationRequest;
import com.spotify.sdk.android.auth.AuthorizationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Response;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;

public class Signup extends AppCompatActivity {
    private SpotifyWrappedViewModel spotifyWrappedViewModel;
    private EditText email;
    private EditText password;
    private ArrayList<Account> accountArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.signup);
        // Initialize Edit Text

        email = (EditText) findViewById(R.id.signup_email);
        password = (EditText) findViewById(R.id.signup_password);

        // Initialize the button
        Button signupBtn = (Button) findViewById(R.id.signup_button);

        SpotifyWrappedDatabase db = SpotifyWrappedDatabase.getInstance(this);
        spotifyWrappedViewModel = new ViewModelProvider(this).get(SpotifyWrappedViewModel.class);

        spotifyWrappedViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                accountArrayList.clear();
                for(Account a: accounts) {
                    accountArrayList.add(a);
                }

            }
        });

        signupBtn.setOnClickListener((v)->{
            Account newAccount = new Account(email.getText().toString(),password.getText().toString());
            spotifyWrappedViewModel.addNewAccount(newAccount);
            email.setText("");
            password.setText("");
            startActivity(new Intent(getApplicationContext(),Login.class));
        });
    }
}