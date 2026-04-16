package com.example.musiclibrary.controller.api;

import com.example.musiclibrary.dto.AlbumRequest;
import com.example.musiclibrary.dto.AlbumResponse;
import com.example.musiclibrary.model.Album;
import com.example.musiclibrary.model.Artist;
import com.example.musiclibrary.repository.AlbumRepository;
import com.example.musiclibrary.repository.ArtistRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumRestController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumRestController(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public List<AlbumResponse> getAllAlbums(@RequestParam(defaultValue = "") String query) {
        return (query.isBlank()
                ? albumRepository.findAll()
                : albumRepository.findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCaseOrderByTitleAsc(query, query))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public AlbumResponse getAlbumById(@PathVariable Long id) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        return toResponse(album);
    }

    @PostMapping
    public ResponseEntity<AlbumResponse> createAlbum(@Valid @RequestBody AlbumRequest request) {
        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));

        Album album = new Album();
        album.setTitle(request.getTitle());
        album.setReleaseYear(request.getReleaseYear());
        album.setArtist(artist);

        Album savedAlbum = albumRepository.save(album);

        return ResponseEntity
                .created(URI.create("/api/albums/" + savedAlbum.getId()))
                .body(toResponse(savedAlbum));
    }

    @PutMapping("/{id}")
    public AlbumResponse updateAlbum(@PathVariable Long id, @Valid @RequestBody AlbumRequest request) {
        Album album = albumRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found"));
        Artist artist = artistRepository.findById(request.getArtistId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));

        album.setTitle(request.getTitle());
        album.setReleaseYear(request.getReleaseYear());
        album.setArtist(artist);

        return toResponse(albumRepository.save(album));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAlbum(@PathVariable Long id) {
        if (!albumRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Album not found");
        }
        albumRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private AlbumResponse toResponse(Album album) {
        return new AlbumResponse(
                album.getId(),
                album.getTitle(),
                album.getReleaseYear(),
                album.getArtist().getId(),
                album.getArtist().getName()
        );
    }
}
