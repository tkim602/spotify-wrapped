package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.media.MediaPlayer;

import androidx.annotation.NonNull;

import com.example.spotifywrapped.Interfaces.Personalization;

import androidx.appcompat.app.AppCompatActivity;

import com.example.spotifywrapped.Models.SpotifyTrackResponse;
import com.example.spotifywrapped.Models.Track;
import com.example.spotifywrapped.R;
import com.bumptech.glide.Glide;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import android.util.Log;
import android.widget.Toast;

import java.io.IOException;


public class TopSongs extends AppCompatActivity {
    private Personalization personalizationService;
    private String AccessToken;
    private String time_range;
    private ImageButton exitButton;
    private ImageButton nextButton;

    private ImageView[] songImageViews = new ImageView[6];
    private TextView[] songTextViews = new TextView[6];
    private int accountId;
    private Retrofit retrofit;
    private MediaPlayer mediaPlayer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs2);
        Bundle gbundle = getIntent().getExtras();
        AccessToken = gbundle.getString("accountToken");
        accountId = gbundle.getInt("accountID");
        time_range = gbundle.getString("timeFrame");
        // View bindings and initialize them
        songImageViews[0] = findViewById(R.id.summary1);
        songImageViews[1] = findViewById(R.id.summary2);
        songImageViews[2] = findViewById(R.id.summary3);
        songImageViews[3] = findViewById(R.id.summary4);
        songImageViews[4] = findViewById(R.id.summary5);
        songImageViews[5] = findViewById(R.id.summary6);

        songTextViews[0] = findViewById(R.id.summary1_name);
        songTextViews[1] = findViewById(R.id.summary2_name);
        songTextViews[2] = findViewById(R.id.summary3_name);
        songTextViews[3] = findViewById(R.id.summary4_name);
        songTextViews[4] = findViewById(R.id.summary5_name);
        songTextViews[5] = findViewById(R.id.summary6_name);
        exitButton = findViewById(R.id.exitButton);
        nextButton = findViewById(R.id.nextButton);


        exitButton.setOnClickListener((v) -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Bundle bundle = new Bundle();
            bundle.putString("accountToken", AccessToken);
            bundle.putInt("accountID", accountId);
            Intent i = new Intent(getApplicationContext(), Generate.class);
            i.putExtras(bundle);
            startActivity(i);

        });

        nextButton.setOnClickListener((v) -> {
            if (mediaPlayer != null) {
                if (mediaPlayer.isPlaying()) {
                    mediaPlayer.stop();
                }
                mediaPlayer.release();
                mediaPlayer = null;
            }
            Bundle bundle = new Bundle();
            bundle.putString("accountToken", AccessToken);
            bundle.putString("timeFrame", time_range);
            bundle.putInt("accountID", accountId);
            Intent i = new Intent(getApplicationContext(), TopArtists.class);
            i.putExtras(bundle);
            startActivity(i);
        });
        setupRetrofit();
        loadTopTracks();
    }

    private void setupRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        personalizationService = retrofit.create(Personalization.class);
    }

    private void loadTopTracks() {
        String authToken = "Bearer " + AccessToken;

        Call<SpotifyTrackResponse> call = personalizationService.getTopTracks(authToken, time_range);
        call.enqueue(new Callback<SpotifyTrackResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpotifyTrackResponse> call, @NonNull Response<SpotifyTrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getItems();
                    updateTopTracksUI(tracks);
                } else {
                    Log.e("TopSongs", "API request failed with code: " + response.code());
                    Toast.makeText(TopSongs.this, "Error loading top songs. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpotifyTrackResponse> call, @NonNull Throwable t) {
                Log.e("TopSongs", "Network request failed", t);
                Toast.makeText(TopSongs.this, "Failed to load top songs. Check your internet connection.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTopTracksUI(List<Track> tracks) {
        int limit = Math.min(tracks.size(), 6);
        for (int i = 0; i < limit; i++) {
            Track track = tracks.get(i);
            songTextViews[i].setText(track.getName());
            Glide.with(this).load(track.getAlbum().getImages().get(0).getUrl()).into(songImageViews[i]);
            if (i == 0 && track.getPreviewUrl() != null) {
                playTopTrack(track.getPreviewUrl());
            }
        }
    }

    private void playTopTrack(String url) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = new MediaPlayer();
        try {
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepareAsync();
            mediaPlayer.setOnPreparedListener(MediaPlayer::start);
        } catch (IOException e) {
            Log.e("TopSongs", "Error setting data source for MediaPlayer", e);
            Toast.makeText(this, "Unable to play the top track.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
            }
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}

