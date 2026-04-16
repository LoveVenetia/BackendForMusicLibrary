package com.example.musiclibrary.repository;

import com.example.musiclibrary.model.Album;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AlbumRepository extends JpaRepository<Album, Long> {

    List<Album> findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCaseOrderByTitleAsc(String title, String artistName);
}
