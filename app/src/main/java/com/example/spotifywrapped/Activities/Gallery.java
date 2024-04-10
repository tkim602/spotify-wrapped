package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.spotifywrapped.Entities.Account;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.SpotifyWrappedDatabase;
import com.example.spotifywrapped.SpotifyWrappedViewModel;
import com.example.spotifywrapped.Utilities.GalleryUtility;

import java.util.ArrayList;
import java.util.List;

public class Gallery extends AppCompatActivity {
    private RecyclerView screenshotRecyclerView;
    private List<String> screenshotPaths;
    private String location;
    private String screenshotPath;

    private String AccessToken;
    private String timeFrame;

    private int accountID;

    private ArrayList<Account> accountArrayList = new ArrayList<>();

    private SpotifyWrappedViewModel spotifyWrappedViewModel;

    private Account currAccount;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getIntent().getExtras();
        accountID = bundle.getInt("accountID");
        SpotifyWrappedDatabase db = SpotifyWrappedDatabase.getInstance(this);
        spotifyWrappedViewModel = new ViewModelProvider(this).get(SpotifyWrappedViewModel.class);

        spotifyWrappedViewModel.getAllAccounts().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                accountArrayList.clear();
                for (Account a : accounts) {
                    accountArrayList.add(a);
                }

                Bundle bundle = getIntent().getExtras();
                accountID = bundle.getInt("accountID");

                for (Account a : accountArrayList) {
                    System.out.println(a.getAccountEmail());
                    if (a.getAccountID() == accountID) {
                        currAccount = a;

                        ImageView[] imageViews = {
                                findViewById(R.id.summary1),
                                findViewById(R.id.summary2),
                                findViewById(R.id.summary3),
                                findViewById(R.id.summary4),
                                findViewById(R.id.summary5),
                                findViewById(R.id.summary6)
                        };
                        ArrayList<String> images = GalleryUtility.extractBase64Strings(currAccount.getAccountImages());

                        for (int i = 0; i < images.size() && i < imageViews.length; i++) {
                            String base64Image = images.get(i);
                            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
                            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                            imageViews[i].setImageBitmap(decodedBitmap);
                        }
                        break;
                    }
                }

            }
        });

        setContentView(R.layout.gallerypage);
    }
}
