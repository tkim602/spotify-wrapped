package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.spotifywrapped.Interfaces.Personalization;
import com.example.spotifywrapped.Models.SpotifyArtistResponse;
import com.example.spotifywrapped.Models.Artist;
import com.example.spotifywrapped.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Summary extends AppCompatActivity {
    private Personalization personalizationService;
    private String AccessToken;
    private String time_range;
    private ImageView[] artistImageViews = new ImageView[6];
    private TextView[] artistTextViews = new TextView[6];

    private ImageButton exitButton;

    private ImageButton nextButton;

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        Bundle gbundle = getIntent().getExtras();
        AccessToken = gbundle.getString("accountToken");
        time_range = gbundle.getString("timeFrame");
        exitButton = findViewById(R.id.exitButton);
        nextButton = findViewById(R.id.nextButton);

        exitButton.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("accountToken", AccessToken);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle);
            startActivity(i);
        });

        nextButton.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putString("accountToken", AccessToken);
            bundle.putString("timeFrame", time_range);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle);
            startActivity(i);
        });

        setupRetrofit();
    }

    private void setupRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        personalizationService = retrofit.create(Personalization.class);
    }

}