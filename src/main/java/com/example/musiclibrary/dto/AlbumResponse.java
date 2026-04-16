package com.example.musiclibrary.dto;

public class AlbumResponse {

    private Long id;
    private String title;
    private int releaseYear;
    private Long artistId;
    private String artistName;

    public AlbumResponse() {
    }

    public AlbumResponse(Long id, String title, int releaseYear, Long artistId, String artistName) {
        this.id = id;
        this.title = title;
        this.releaseYear = releaseYear;
        this.artistId = artistId;
        this.artistName = artistName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getReleaseYear() {
        return releaseYear;
    }

    public void setReleaseYear(int releaseYear) {
        this.releaseYear = releaseYear;
    }

    public Long getArtistId() {
        return artistId;
    }

    public void setArtistId(Long artistId) {
        this.artistId = artistId;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
