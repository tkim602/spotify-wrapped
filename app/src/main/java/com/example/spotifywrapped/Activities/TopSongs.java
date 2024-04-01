package com.example.spotifywrapped.Activities;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

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


public class TopSongs extends AppCompatActivity {
    private Personalization personalizationService;
    private String AccessToken;
    private String time_range;
    private ImageView[] songImageViews = new ImageView[6];
    private TextView[] songTextViews = new TextView[6];

    private Retrofit retrofit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.top_songs2);
        Bundle bundle = getIntent().getExtras();
        AccessToken = bundle.getString("accountToken");
        time_range = bundle.getString("timeFrame");
        // View bindings and initialize them
        songImageViews[0] = findViewById(R.id.song1);
        songImageViews[1] = findViewById(R.id.song2);
        songImageViews[2] = findViewById(R.id.song3);
        songImageViews[3] = findViewById(R.id.song4);
        songImageViews[4] = findViewById(R.id.song5);
        songImageViews[5] = findViewById(R.id.song6);

        songTextViews[0] = findViewById(R.id.song1_name);
        songTextViews[1] = findViewById(R.id.song2_name);
        songTextViews[2] = findViewById(R.id.song3_name);
        songTextViews[3] = findViewById(R.id.song4_name);
        songTextViews[4] = findViewById(R.id.song5_name);
        songTextViews[5] = findViewById(R.id.song6_name);

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
        // dont know how to access to mAccessToken
        String authToken = "Bearer " + "token";

        Call<SpotifyTrackResponse> call = personalizationService.getTopTracks(authToken);
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
        }
    }
}
