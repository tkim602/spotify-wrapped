package com.example.spotifywrapped.Interfaces;

import com.example.spotifywrapped.Models.SpotifyTrackResponse;
import com.example.spotifywrapped.Models.SpotifyArtistResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Header;

public interface Personalization {
    @GET("v1/me/top/tracks?limit=6")
    Call<SpotifyTrackResponse> getTopTracks(@Header("Authorization") String authToken);

    @GET("v1/me/top/artists?limit=6")
    Call<SpotifyArtistResponse> getTopArtists(@Header("Authorization") String authToken);

}
