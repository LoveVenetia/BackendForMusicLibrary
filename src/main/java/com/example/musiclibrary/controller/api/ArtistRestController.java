package com.example.musiclibrary.controller.api;

import com.example.musiclibrary.dto.ArtistRequest;
import com.example.musiclibrary.dto.ArtistResponse;
import com.example.musiclibrary.model.Artist;
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
@RequestMapping("/api/artists")
public class ArtistRestController {

    private final ArtistRepository artistRepository;

    public ArtistRestController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping
    public List<ArtistResponse> getAllArtists(@RequestParam(defaultValue = "") String query) {
        return (query.isBlank()
                ? artistRepository.findAll()
                : artistRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query))
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @GetMapping("/{id}")
    public ArtistResponse getArtistById(@PathVariable Long id) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
        return toResponse(artist);
    }

    @PostMapping
    public ResponseEntity<ArtistResponse> createArtist(@Valid @RequestBody ArtistRequest request) {
        Artist artist = new Artist();
        artist.setName(request.getName());
        Artist savedArtist = artistRepository.save(artist);
        return ResponseEntity
                .created(URI.create("/api/artists/" + savedArtist.getId()))
                .body(toResponse(savedArtist));
    }

    @PutMapping("/{id}")
    public ArtistResponse updateArtist(@PathVariable Long id, @Valid @RequestBody ArtistRequest request) {
        Artist artist = artistRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found"));
        artist.setName(request.getName());
        return toResponse(artistRepository.save(artist));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteArtist(@PathVariable Long id) {
        if (!artistRepository.existsById(id)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Artist not found");
        }
        artistRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ArtistResponse toResponse(Artist artist) {
        return new ArtistResponse(artist.getId(), artist.getName());
    }
}
