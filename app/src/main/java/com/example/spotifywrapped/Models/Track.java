package com.example.spotifywrapped.Models;

import java.util.List;

public class Track {
    private String name;
    private Album album;
    private List<Artist> artists;
    private String preview_url;

    private int duration_ms;

    // Getter and setter
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Album getAlbum() {
        return album;
    }

    public void setAlbum(Album album) {
        this.album = album;
    }

    public List<Artist> getArtists() {
        return artists;
    }

    public void setArtists(List<Artist> artists) {
        this.artists = artists;
    }

    public String getPreviewUrl() {
        return preview_url;
    }
    public void setPreview_url(String preview_url) {
        this.preview_url = preview_url;
    }

    public int getDurationMs() {
        return duration_ms;
    }
    public void setDurationMs(int duration_ms) {
        this.duration_ms = duration_ms;
    }
}
