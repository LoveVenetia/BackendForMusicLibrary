package com.example.musiclibrary.controller.api;

import com.example.musiclibrary.dto.ArtistRequest;
import com.example.musiclibrary.model.Artist;
import com.example.musiclibrary.repository.ArtistRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ArtistRestControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ArtistRepository artistRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private Artist testArtist;

    @BeforeEach
    void setUp() {
        artistRepository.deleteAll();
        testArtist = new Artist("Test Artist");
        testArtist = artistRepository.save(testArtist);
    }

    @Test
    void testUnauthenticatedUserCannotAccessAPI() throws Exception {
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCanGetArtists() throws Exception {
        mockMvc.perform(get("/api/artists"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Test Artist"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCannotPostArtist() throws Exception {
        ArtistRequest request = new ArtistRequest();
        request.setName("New Artist");

        mockMvc.perform(post("/api/artists")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN", "USER"})
    void testAdminCanUpdateArtist() throws Exception {
        ArtistRequest request = new ArtistRequest();
        request.setName("Updated Artist");

        mockMvc.perform(put("/api/artists/{id}", testArtist.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Artist"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void testUserCannotDeleteArtist() throws Exception {
        mockMvc.perform(delete("/api/artists/{id}", testArtist.getId()))
                .andExpect(status().isForbidden());
    }
}
