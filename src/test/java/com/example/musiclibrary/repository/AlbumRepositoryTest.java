package com.example.musiclibrary.repository;

import com.example.musiclibrary.model.Album;
import com.example.musiclibrary.model.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AlbumRepositoryTest {

    @Autowired
    private AlbumRepository albumRepository;

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void shouldSaveAlbumWithArtist() {
        Artist artist = artistRepository.save(new Artist("Radiohead"));

        Album album = new Album("OK Computer", 1997, artist);
        Album savedAlbum = albumRepository.save(album);
        Optional<Album> foundAlbum = albumRepository.findById(savedAlbum.getId());

        assertThat(savedAlbum.getId()).isNotNull();
        assertThat(foundAlbum).isPresent();
        assertThat(foundAlbum.get().getTitle()).isEqualTo("OK Computer");
        assertThat(foundAlbum.get().getReleaseYear()).isEqualTo(1997);
        assertThat(foundAlbum.get().getArtist().getName()).isEqualTo("Radiohead");
    }

    @Test
    void shouldFindAlbumsByTitleOrArtistNameIgnoringCase() {
        Artist radiohead = artistRepository.save(new Artist("Radiohead"));
        Artist portishead = artistRepository.save(new Artist("Portishead"));
        albumRepository.save(new Album("OK Computer", 1997, radiohead));
        albumRepository.save(new Album("Dummy", 1994, portishead));

        List<Album> byTitle = albumRepository
                .findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCaseOrderByTitleAsc("computer", "computer");
        List<Album> byArtist = albumRepository
                .findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCaseOrderByTitleAsc("radio", "radio");

        assertThat(byTitle).hasSize(1);
        assertThat(byTitle.get(0).getTitle()).isEqualTo("OK Computer");
        assertThat(byArtist).hasSize(1);
        assertThat(byArtist.get(0).getArtist().getName()).isEqualTo("Radiohead");
    }
}
