package com.avaliacao.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
 

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
 

@SpringBootTest(classes = {com.avaliacao.AvaliacaoApplication.class, com.avaliacao.config.AvaliacaoTestConfiguration.class})
@AutoConfigureMockMvc
public class MovieControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private com.avaliacao.service.MovieService movieService;

    @BeforeEach
    void setUp() throws Exception {
        movieService.loadMoviesFromCsv();
    }

    @Test
    void listAllMovies_andFavicon() throws Exception {
    mockMvc.perform(get("/api/movies"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0]").exists());
    }

      @Test
    void getMovieById_found_and_notFound() throws Exception {
    mockMvc.perform(get("/api/movies/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id").value(1));

    mockMvc.perform(get("/api/movies/999999"))
        .andExpect(status().isNotFound());
    }


    @Test
    void options_shouldContainAllowHeader() throws Exception {
    mockMvc.perform(options("/api/movies"))
        .andExpect(status().isOk())
        .andExpect(result -> {
            String allow = result.getResponse().getHeader("Allow");
            org.junit.jupiter.api.Assertions.assertNotNull(allow, "Allow header should be present");
            org.junit.jupiter.api.Assertions.assertTrue(allow.contains("GET"), "Allow header should contain GET");
            org.junit.jupiter.api.Assertions.assertFalse(allow.contains("POST"), "Allow header should not contain POST");
        });
    }

    @Test
    void producersIntervals_returnsMinMaxLists() throws Exception {
    mockMvc.perform(get("/api/movies/producers/intervals"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.min").isArray())
        .andExpect(jsonPath("$.max").isArray());
    }
}
