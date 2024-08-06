package com.example.simplemusicplayersmp;

public class MusicFiles {
    private String path_MOA;
    private String title_MOA;
    private String artist_MOA;
    private String album_MOA;
    private String duration_MOA;
    private String id_MOA;

    public MusicFiles(String path_MOA, String title_MOA, String artist_MOA, String album_MOA, String duration_MOA, String id_MOA) {
        this.path_MOA = path_MOA;
        this.title_MOA = title_MOA;
        this.artist_MOA = artist_MOA;
        this.album_MOA = album_MOA;
        this.duration_MOA = duration_MOA;
        this.id_MOA = id_MOA;
    }

    public MusicFiles() {
    }

    public String getPath_MOA() {
        return path_MOA;
    }

    public void setPath_MOA(String path_MOA) {
        this.path_MOA = path_MOA;
    }

    public String getTitle_MOA() {
        return title_MOA;
    }

    public void setTitle_MOA(String title_MOA) {
        this.title_MOA = title_MOA;
    }

    public String getArtist_MOA() {
        return artist_MOA;
    }

    public void setArtist_MOA(String artist_MOA) {
        this.artist_MOA = artist_MOA;
    }

    public String getAlbum_MOA() {
        return album_MOA;
    }

    public void setAlbum_MOA(String album_MOA) {
        this.album_MOA = album_MOA;
    }

    public String getDuration_MOA() {
        return duration_MOA;
    }

    public void setDuration_MOA(String duration_MOA) {
        this.duration_MOA = duration_MOA;
    }

    public String getId_MOA() {
        return id_MOA;
    }

    public void setId_MOA(String id_MOA) {
        this.id_MOA = id_MOA;
    }
}
