package com.example.musiclibrary.controller;

import com.example.musiclibrary.model.Artist;
import com.example.musiclibrary.repository.ArtistRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class ArtistControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    private Artist testArtist;

    @BeforeEach
    void setUp() {
        artistRepository.deleteAll();
        testArtist = new Artist("Test Artist");
        testArtist = artistRepository.save(testArtist);
    }

    @Test
    void testLoginPageAccessible() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    void testUnauthenticatedUserRedirectedToLogin() throws Exception {
        mockMvc.perform(get("/artists"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login**"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCanViewArtists() throws Exception {
        mockMvc.perform(get("/artists"))
                .andExpect(status().isOk())
                .andExpect(view().name("artists"))
                .andExpect(model().attributeExists("artists"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCannotCreateArtist() throws Exception {
        mockMvc.perform(get("/artists/new"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    void testAdminCanAccessCreateForm() throws Exception {
        mockMvc.perform(get("/artists/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("artist-form"))
                .andExpect(model().attributeExists("artist"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    void testAdminCanCreateArtist() throws Exception {
        mockMvc.perform(post("/artists")
                        .param("name", "New Artist")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/artists"));

        org.assertj.core.api.Assertions.assertThat(artistRepository.count()).isEqualTo(2);
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCannotDeleteArtist() throws Exception {
        mockMvc.perform(post("/artist/{id}/delete", testArtist.getId())
                        .with(csrf()))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    void testAdminCanDeleteArtist() throws Exception {
        mockMvc.perform(post("/artist/{id}/delete", testArtist.getId())
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/artists"));

        org.assertj.core.api.Assertions.assertThat(artistRepository.count()).isZero();
    }
}
