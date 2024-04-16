package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
                        screenshotPaths = GalleryUtility.extractBase64Strings(currAccount.getAccountImages());

                        ImageView[] imageViews = {
                                findViewById(R.id.summary1),
                                findViewById(R.id.summary2),
                                findViewById(R.id.summary3),
                                findViewById(R.id.summary4),
                                findViewById(R.id.summary5),
                                findViewById(R.id.summary6)
                        };
                        TextView[] orange = {
                                findViewById(R.id.color_0),
                                findViewById(R.id.color_1),
                                findViewById(R.id.color_2),
                                findViewById(R.id.color_3),
                                findViewById(R.id.color_4),
                                findViewById(R.id.color_5)
                        };
                        TextView[] blue = {
                                findViewById(R.id.bluecolor_0),
                                findViewById(R.id.bluecolor_1),
                                findViewById(R.id.bluecolor_2),
                                findViewById(R.id.bluecolor_3),
                                findViewById(R.id.bluecolor_4),
                                findViewById(R.id.bluecolor_5)
                        };
                        ArrayList<String> images = GalleryUtility.extractBase64Strings(currAccount.getAccountImages());
                        for (int i = 0; i < images.size() && i < imageViews.length; i++) {
                            String screenshotPath = screenshotPaths.get(i);
                            Bitmap bitmap = BitmapFactory.decodeFile(screenshotPath);
                            imageViews[i].setImageBitmap(Bitmap.createScaledBitmap(bitmap, 500, 900, false));
                            if (i + 1 >= images.size() || i + 1 >= imageViews.length) {
                                for (int j = 0; j <= i && j < orange.length && j < blue.length; j++) {
                                    TextView t = (TextView) orange[j];
                                    t.setVisibility(View.VISIBLE);
                                    TextView t2 = (TextView) blue[j];
                                    t2.setVisibility(View.VISIBLE);
                                }
                            }
                        }

                        break;
                    }
                }

            }
        });

        setContentView(R.layout.gallerypage);
        ImageButton exit = (ImageButton) findViewById(R.id.imageButton2);
        exit.setOnClickListener((v) -> {
            Bundle bundle1 = new Bundle();
            bundle1.putInt("accountID", accountID);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle1);
            startActivity(i);
        });
    }
}
