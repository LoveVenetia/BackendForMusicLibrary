package com.example.musiclibrary.controller;

import com.example.musiclibrary.model.Album;
import com.example.musiclibrary.model.Artist;
import com.example.musiclibrary.repository.ArtistRepository;
import com.example.musiclibrary.repository.AlbumRepository;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping
public class AlbumController {

    private final AlbumRepository albumRepository;
    private final ArtistRepository artistRepository;

    public AlbumController(AlbumRepository albumRepository, ArtistRepository artistRepository) {
        this.albumRepository = albumRepository;
        this.artistRepository = artistRepository;
    }

    @GetMapping("/albums")
    public String listAlbums(@RequestParam(defaultValue = "") String query, Model model) {
        List<Album> albums = query.isBlank()
                ? albumRepository.findAll()
                : albumRepository.findByTitleContainingIgnoreCaseOrArtist_NameContainingIgnoreCaseOrderByTitleAsc(query, query);
        model.addAttribute("albums", albums);
        model.addAttribute("query", query);
        return "albums";
    }

    @GetMapping("/albums/new")
    public String showCreateForm(Model model) {
        model.addAttribute("album", new Album());
        addArtistsToModel(model);
        return "album-form";
    }

    @PostMapping("/albums")
    public String createAlbum(@Valid @ModelAttribute("album") Album album,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long artistId,
                              Model model) {
        Artist artist = resolveArtist(artistId);
        if (artist == null) {
            bindingResult.rejectValue("artist", "NotNull", "Artist is required");
        } else {
            album.setArtist(artist);
        }
        if (bindingResult.hasErrors()) {
            addArtistsToModel(model);
            return "album-form";
        }
        albumRepository.save(album);
        return "redirect:/albums";
    }

    @GetMapping("/album/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("album", albumRepository.findById(id).orElseThrow());
        addArtistsToModel(model);
        return "album-form";
    }

    @PostMapping("/album/{id}")
    public String updateAlbum(@PathVariable Long id,
                              @Valid @ModelAttribute("album") Album album,
                              BindingResult bindingResult,
                              @RequestParam(required = false) Long artistId,
                              Model model) {
        Artist artist = resolveArtist(artistId);
        if (artist == null) {
            bindingResult.rejectValue("artist", "NotNull", "Artist is required");
        } else {
            album.setArtist(artist);
        }
        if (bindingResult.hasErrors()) {
            album.setId(id);
            addArtistsToModel(model);
            return "album-form";
        }
        Album existingAlbum = albumRepository.findById(id).orElseThrow();
        existingAlbum.setTitle(album.getTitle());
        existingAlbum.setReleaseYear(album.getReleaseYear());
        existingAlbum.setArtist(album.getArtist());
        albumRepository.save(existingAlbum);
        return "redirect:/albums";
    }

    @PostMapping("/album/{id}/delete")
    public String deleteAlbum(@PathVariable Long id) {
        albumRepository.deleteById(id);
        return "redirect:/albums";
    }

    private void addArtistsToModel(Model model) {
        model.addAttribute("artists", artistRepository.findAll());
    }

    private Artist resolveArtist(Long artistId) {
        if (artistId == null) {
            return null;
        }
        return artistRepository.findById(artistId).orElse(null);
    }
}
