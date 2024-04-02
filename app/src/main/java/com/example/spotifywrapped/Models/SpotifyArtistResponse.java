package com.example.spotifywrapped.Models;

import java.util.List;

public class SpotifyArtistResponse {
    private List<Artist> items;

    // Getter and setter
    public List<Artist> getItems() {
        return items;
    }

    public void setItems(List<Artist> items) {
        this.items = items;
    }


}