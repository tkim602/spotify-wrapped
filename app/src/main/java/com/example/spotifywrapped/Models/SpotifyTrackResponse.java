package com.example.spotifywrapped.Models;

import java.util.List;

public class SpotifyTrackResponse {
    private List<Track> items;

    // Getter and setter
    public List<Track> getItems() {
        return items;
    }

    public void setItems(List<Track> items) {
        this.items = items;
    }
}
