package com.example.spotifywrapped.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.spotifywrapped.Entities.Account;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.SpotifyWrappedDatabase;
import com.example.spotifywrapped.SpotifyWrappedViewModel;

import java.util.ArrayList;
import java.util.List;

public class Login extends AppCompatActivity {
    private SpotifyWrappedViewModel spotifyWrappedViewModel;
    private EditText email;
    private EditText password;
    private ArrayList<Account> accountArrayList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        SpotifyWrappedDatabase db = SpotifyWrappedDatabase.getInstance(this);
        spotifyWrappedViewModel = new ViewModelProvider(this).get(SpotifyWrappedViewModel.class);

        spotifyWrappedViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                accountArrayList.clear();
                accountArrayList.addAll(accounts);
            }
        });

        Button loginBtn = (Button) findViewById(R.id.login_button);
        email = (EditText) findViewById(R.id.loginEmail);
        password = (EditText) findViewById(R.id.loginPassword);

        loginBtn.setOnClickListener((v)->{
            boolean success = false;
            String e = email.getText().toString();
            String p = password.getText().toString();

            for (Account a : accountArrayList) {
                if (a.getAccountEmail().equals(e) && a.getAccountPassword().equals(p)) {
                    success = true;
                    System.out.println("success");
                    break;
                    // do stuff to login the user in
                }
            }

            if (!success) {
                // display error
                System.out.println("user does not exist");
            }
        });
    }
}