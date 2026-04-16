package com.example.musiclibrary.repository;

import com.example.musiclibrary.model.Artist;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ArtistRepositoryTest {

    @Autowired
    private ArtistRepository artistRepository;

    @Test
    void shouldSaveArtist() {
        Artist artist = new Artist("Daft Punk");

        Artist savedArtist = artistRepository.save(artist);
        Optional<Artist> foundArtist = artistRepository.findById(savedArtist.getId());

        assertThat(savedArtist.getId()).isNotNull();
        assertThat(foundArtist).isPresent();
        assertThat(foundArtist.get().getName()).isEqualTo("Daft Punk");
    }

    @Test
    void shouldFindArtistsByNameIgnoringCase() {
        artistRepository.save(new Artist("Daft Punk"));
        artistRepository.save(new Artist("Massive Attack"));

        List<Artist> artists = artistRepository.findByNameContainingIgnoreCaseOrderByNameAsc("daft");

        assertThat(artists).hasSize(1);
        assertThat(artists.get(0).getName()).isEqualTo("Daft Punk");
    }
}
