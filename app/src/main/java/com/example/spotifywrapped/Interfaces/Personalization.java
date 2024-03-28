package com.example.spotifywrapped.Interfaces;

import com.example.spotifywrapped.Models.SpotifyTrackResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Personalization {
    @GET("v1/me/top/tracks?limit=5")
    Call<SpotifyTrackResponse> getTopTracks(@Header("Authorization") String authToken);
}
