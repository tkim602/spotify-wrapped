package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.media.MediaPlayer;
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
import com.example.spotifywrapped.Models.SpotifyTrackResponse;
import com.example.spotifywrapped.Models.Track;
import com.example.spotifywrapped.R;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class TopArtists extends AppCompatActivity {
    private Personalization personalizationService;
    private String AccessToken;
    private String time_range;
    private ImageView[] artistImageViews = new ImageView[6];
    private TextView[] artistTextViews = new TextView[6];
    private int accountId;
    private ImageButton exitButton;

    private ImageButton nextButton;

    private Retrofit retrofit;

    private MediaPlayer mediaPlayer;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_artists);
        Bundle gbundle = getIntent().getExtras();
        AccessToken = gbundle.getString("accountToken");
        time_range = gbundle.getString("timeFrame");
        accountId = gbundle.getInt("accountID");
        exitButton = findViewById(R.id.imageButton2);
        nextButton = findViewById(R.id.nextButton);
        // View bindings and initialize them
        artistImageViews[0] = findViewById(R.id.artist_1);
        artistImageViews[1] = findViewById(R.id.artist_2);
        artistImageViews[2] = findViewById(R.id.artist_3);
        artistImageViews[3] = findViewById(R.id.artist_4);
        artistImageViews[4] = findViewById(R.id.artist_5);
        artistImageViews[5] = findViewById(R.id.artist_6);

        artistTextViews[0] = findViewById(R.id.artist1_name);
        artistTextViews[1] = findViewById(R.id.artist2_name);
        artistTextViews[2] = findViewById(R.id.artist3_name);
        artistTextViews[3] = findViewById(R.id.artist4_name);
        artistTextViews[4] = findViewById(R.id.artist5_name);
        artistTextViews[5] = findViewById(R.id.artist6_name);

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
            Intent i = new Intent(getApplicationContext(), GameOne.class);
            i.putExtras(bundle);
            startActivity(i);
        });
        setupRetrofit();
        loadTopArtists();
    }

    private void setupRetrofit() {
        retrofit = new Retrofit.Builder()
                .baseUrl("https://api.spotify.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        personalizationService = retrofit.create(Personalization.class);
    }

    private void loadTopArtists() {
        String authToken = "Bearer " + AccessToken;
        Call<SpotifyTrackResponse> call1 = personalizationService.getTopTracks(authToken, time_range);
        call1.enqueue(new Callback<SpotifyTrackResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpotifyTrackResponse> call1, @NonNull Response<SpotifyTrackResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Track> tracks = response.body().getItems();
                    playTopTrack(tracks.get(1).getPreviewUrl());
                } else {
                    Log.e("TopSongs", "API request failed with code: " + response.code());
                    Toast.makeText(TopArtists.this, "Error loading top songs. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpotifyTrackResponse> call1, @NonNull Throwable t) {
                Log.e("TopSongs", "Network request failed", t);
                Toast.makeText(TopArtists.this, "Failed to load top songs. Check your internet connection.", Toast.LENGTH_LONG).show();
            }
        });
        Call<SpotifyArtistResponse> call = personalizationService.getTopArtists(authToken, time_range);
        call.enqueue(new Callback<SpotifyArtistResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpotifyArtistResponse> call, @NonNull Response<SpotifyArtistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Artist> artists = response.body().getItems();
                    updateTopArtistsUI(artists);
                } else {
                    Log.e("TopArtists", "API request failed with code: " + response.code());
                    Toast.makeText(TopArtists.this, "Error loading top artists. Please try again later.", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpotifyArtistResponse> call, @NonNull Throwable t) {
                Log.e("TopArtists", "Network request failed", t);
                Toast.makeText(TopArtists.this, "Failed to load top artists. Check your internet connection.", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateTopArtistsUI(List<Artist> artists) {
        int limit = Math.min(artists.size(), 6);
        for (int i = 0; i < limit; i++) {
            Artist artist = artists.get(i);
            artistTextViews[i].setText(artist.getName());
            System.out.println(artist.getUrl());
            if (artist != null) {
                Glide.with(this).load(artist.getImages().get(0).getUrl()).into(artistImageViews[i]);
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