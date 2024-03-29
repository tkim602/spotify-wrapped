package com.example.spotifywrapped.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.spotifywrapped.Entities.Account;
import com.example.spotifywrapped.R;

import com.example.spotifywrapped.SpotifyWrappedDatabase;
import com.example.spotifywrapped.SpotifyWrappedViewModel;
import com.example.spotifywrapped.databinding.ActivitySettingsBinding;

import java.util.ArrayList;
import java.util.List;

public class Settings extends AppCompatActivity {

    private EditText editEmail;
    private EditText editPassword;
    private SpotifyWrappedViewModel spotifyWrappedViewModel;
    private ArrayList<Account> accountArrayList = new ArrayList<>();
    private Account currAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editEmail = (EditText) findViewById(R.id.edit_email);
        editPassword = (EditText) findViewById(R.id.edit_password);

        SpotifyWrappedDatabase db = SpotifyWrappedDatabase.getInstance(this);
        spotifyWrappedViewModel = new ViewModelProvider(this).get(SpotifyWrappedViewModel.class);

        spotifyWrappedViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                accountArrayList.clear();
                accountArrayList.addAll(accounts);

                Bundle bundle = getIntent().getExtras();
                String temp = "email";
                String accountEmail = bundle.getString(temp);

                for (Account a : accountArrayList) {
                    if (a.getAccountEmail().equals(accountEmail)) {
                        currAccount = a;
                        break;
                    }
                }

                if (currAccount != null) {
                    String e = currAccount.getAccountEmail();
                    editEmail.setText(e);
                    editPassword.setText(currAccount.getAccountPassword());
                }
            }

        });

        Button saveButton = (Button) findViewById(R.id.save_changes);
        saveButton.setOnClickListener((v)->{
            String editedEmail = editEmail.getText().toString();
            String editedPassword = editPassword.getText().toString();
            editEmail.setText("");
            editPassword.setText("");
        });

        Button deleteButton = (Button) findViewById(R.id.delete_account);
        deleteButton.setOnClickListener((v)->{
            spotifyWrappedViewModel.removeAccount(currAccount);
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
        });

        // detecting toggle

//        ActivitySettingsBinding binding = ActivitySettingsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//
//        binding.materialSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
//            if (isChecked) {
//                System.out.println("checked");
//            } else {
//                System.out.println("unchecked");
//            }
//        });
    }
}