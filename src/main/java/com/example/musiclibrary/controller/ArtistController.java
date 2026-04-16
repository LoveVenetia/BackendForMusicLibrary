package com.example.musiclibrary.controller;

import com.example.musiclibrary.model.Artist;
import jakarta.validation.Valid;
import com.example.musiclibrary.repository.ArtistRepository;
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
public class ArtistController {

    private final ArtistRepository artistRepository;

    public ArtistController(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    @GetMapping("/artists")
    public String listArtists(@RequestParam(defaultValue = "") String query, Model model) {
        List<Artist> artists = query.isBlank()
                ? artistRepository.findAll()
                : artistRepository.findByNameContainingIgnoreCaseOrderByNameAsc(query);
        model.addAttribute("artists", artists);
        model.addAttribute("query", query);
        return "artists";
    }

    @GetMapping("/artist/{id}")
    public String artistDetails(@PathVariable Long id, Model model) {
        model.addAttribute("artist", artistRepository.findById(id).orElseThrow());
        return "artist-details";
    }

    @GetMapping("/artists/new")
    public String showCreateForm(Model model) {
        model.addAttribute("artist", new Artist());
        return "artist-form";
    }

    @PostMapping("/artists")
    public String createArtist(@Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "artist-form";
        }
        artistRepository.save(artist);
        return "redirect:/artists";
    }

    @GetMapping("/artist/{id}/edit")
    public String showEditForm(@PathVariable Long id, Model model) {
        model.addAttribute("artist", artistRepository.findById(id).orElseThrow());
        return "artist-form";
    }

    @PostMapping("/artist/{id}")
    public String updateArtist(@PathVariable Long id, @Valid @ModelAttribute("artist") Artist artist, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            artist.setId(id);
            return "artist-form";
        }
        Artist existingArtist = artistRepository.findById(id).orElseThrow();
        existingArtist.setName(artist.getName());
        artistRepository.save(existingArtist);
        return "redirect:/artists";
    }

    @PostMapping("/artist/{id}/delete")
    public String deleteArtist(@PathVariable Long id) {
        artistRepository.deleteById(id);
        return "redirect:/artists";
    }
}
