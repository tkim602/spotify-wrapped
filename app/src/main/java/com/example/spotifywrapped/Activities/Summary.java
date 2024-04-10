package com.example.spotifywrapped.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.example.spotifywrapped.Entities.Account;
import com.example.spotifywrapped.Models.Artist;
import com.bumptech.glide.Glide;
import com.example.spotifywrapped.Interfaces.Personalization;
import com.example.spotifywrapped.Models.SpotifyArtistResponse;
import com.example.spotifywrapped.Models.SpotifyTrackResponse;
import com.example.spotifywrapped.Models.Track;
import com.example.spotifywrapped.R;
import com.example.spotifywrapped.SpotifyWrappedDatabase;
import com.example.spotifywrapped.SpotifyWrappedViewModel;
import com.example.spotifywrapped.Utilities.GalleryUtility;

import org.checkerframework.checker.units.qual.C;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class Summary extends AppCompatActivity {
    private Personalization personalizationService;
    private String AccessToken;
    private String time_range;
    private TextView[] topsongTextViews = new TextView[6];
    private TextView[] artistTextViews = new TextView[6];
    private TextView genreTextView;
    private TextView minutesTextView;
    private ImageView artistImageView;

    private ImageButton exitButton;

    private ImageButton nextButton;
    private int accountID;

    private Retrofit retrofit;
    private MediaPlayer mediaPlayer;

    private Account currAccount;

    private SpotifyWrappedViewModel spotifyWrappedViewModel;

    private ArrayList<Account> accountArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);
        Bundle gbundle = getIntent().getExtras();
        AccessToken = gbundle.getString("accountToken");
        time_range = gbundle.getString("timeFrame");
        accountID = gbundle.getInt("accountID");
        exitButton = findViewById(R.id.exitButton);
        nextButton = findViewById(R.id.nextButton);

        topsongTextViews[0] = findViewById(R.id.topsong_update1);
        topsongTextViews[1] = findViewById(R.id.topsong_update2);
        topsongTextViews[2] = findViewById(R.id.topsong_update3);
        topsongTextViews[3] = findViewById(R.id.topsong_update4);
        topsongTextViews[4] = findViewById(R.id.topsong_update5);
        topsongTextViews[5] = findViewById(R.id.topsong_update6);

        artistTextViews[0] = findViewById(R.id.topartist_update1);
        artistTextViews[1] = findViewById(R.id.topartist_update2);
        artistTextViews[2] = findViewById(R.id.topartist_update3);
        artistTextViews[3] = findViewById(R.id.topartist_update4);
        artistTextViews[4] = findViewById(R.id.topartist_update5);
        artistTextViews[5] = findViewById(R.id.topartist_update6);

        genreTextView = findViewById(R.id.genretobeupdated);
        minutesTextView = findViewById(R.id.minutesToBeUpdated);
        artistImageView = findViewById(R.id.artistImage);

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
                        break;
                    }
                }
            }
        });

        exitButton.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("accountID", accountID);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle);

            ArrayList<String> currentImages = GalleryUtility.extractBase64Strings(currAccount.getAccountImages());
            currentImages.add(captureScreenshot());
            currAccount.setAccountImages(GalleryUtility.encodeBase64ImagesToString(currentImages));
            spotifyWrappedViewModel.updateAccount(currAccount);

            startActivity(i);
            finish();
        });

        nextButton.setOnClickListener((v) -> {
            Bundle bundle = new Bundle();
            bundle.putInt("accountID", accountID);
            Intent i = new Intent(getApplicationContext(), Homepage.class);
            i.putExtras(bundle);
            ArrayList<String> currentImages = GalleryUtility.extractBase64Strings(currAccount.getAccountImages());
            currentImages.add(captureScreenshot());
            currAccount.setAccountImages(GalleryUtility.encodeBase64ImagesToString(currentImages));
            spotifyWrappedViewModel.updateAccount(currAccount);

            startActivity(i);
            finish();
        });

        setupRetrofit();
        loadTopTractsAndArtists();
    }

    private void loadTopTractsAndArtists() {
        loadTopTracks();
        loadTopArtist();
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
                    int totalListeningMinutes = calculateListeningMinutes(tracks);
                    updateListeningMinutesUI(totalListeningMinutes);
                } else {
                    Log.e("Summary", "Failed to load top tracks");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpotifyTrackResponse> call, @NonNull Throwable t) {
                Log.e("Summary", "Error loading top tracks", t);
            }
        });
    }

    private void updateTopTracksUI(List<Track> tracks) {
        int limit = Math.min(tracks.size(), 6);
        for (int i = 0; i < limit; i++) {
            Track track = tracks.get(i);
            topsongTextViews[i].setText(track.getName());
        }
    }

    private void loadTopArtist() {
        String authToken = "Bearer " + AccessToken;

        Call<SpotifyArtistResponse> call = personalizationService.getTopArtists(authToken, time_range);
        call.enqueue(new Callback<SpotifyArtistResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpotifyArtistResponse> call, @NonNull Response<SpotifyArtistResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Artist> artists = response.body().getItems();
                    String topGenre = calculateTopGenre(response.body().getItems());
                    updateGenreUI(topGenre);
                    updateTopArtistsUI(artists);
                } else {
                    Log.e("Summary", "Failed to load top artists");
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpotifyArtistResponse> call, @NonNull Throwable t) {
                Log.e("Summary", "Error loading top artists", t);
            }
        });
    }

    private void updateTopArtistsUI(List<Artist> artists) {
        int limit = Math.min(artists.size(), 6);
        for (int i = 0; i < limit; i++) {
            Artist artist = artists.get(i);
            artistTextViews[i].setText(artist.getName());
            if ((artist != null) && (i == 0)) {
                Glide.with(this).load(artist.getImages().get(0).getUrl()).into(artistImageView);
            }
        }
    }

    private int calculateListeningMinutes(List<Track> tracks) {
        int totalDurationMs = tracks.stream().mapToInt(Track::getDurationMs).sum();
        return totalDurationMs / (1000 * 60);
    }

    private String calculateTopGenre(List<Artist> artists) {
        Map<String, Integer> genreCount = new HashMap<>();
        artists.forEach(artist -> artist.getGenres().forEach(genre -> genreCount.put(genre, genreCount.getOrDefault(genre, 0) + 1)));

        return genreCount.entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .orElse("Unknown Genre");
    }

    private void updateGenreUI(String genre) {
        genreTextView.setText(genre);
    }

    private void updateListeningMinutesUI(int minutes) {
        minutesTextView.setText(String.format("%d minutes", minutes));
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
            Log.e("Summary", "Error setting data source for MediaPlayer.");
        }
    }

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

    public String captureScreenshot() {
        View rootView = getWindow().getDecorView().getRootView();
        Bitmap bitmap = Bitmap.createBitmap(rootView.getWidth(), rootView.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        rootView.draw(canvas);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        byte[] byteArray = outputStream.toByteArray();

        return Base64.encodeToString(byteArray, Base64.NO_WRAP);
    }


}