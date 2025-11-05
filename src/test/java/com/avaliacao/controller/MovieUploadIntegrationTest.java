package com.avaliacao.controller;

import com.avaliacao.repository.MovieRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(classes = {com.avaliacao.AvaliacaoApplication.class, com.avaliacao.config.AvaliacaoTestConfiguration.class})
@AutoConfigureMockMvc
class MovieUploadIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MovieRepository movieRepository;

    @Test
    public void whenUploadCsv_thenOverwriteAndReturnTotal() throws Exception {
        
        String csv = "year;title;studios;producers;winner\n"
                + "2001;Upload Movie A;Studio A;Producer A;yes\n"
                + "2002;Upload Movie B;Studio B;Producer B;no\n";

        MockMultipartFile file = new MockMultipartFile("file", "upload.csv", "text/csv",
                csv.getBytes(StandardCharsets.UTF_8));

        MediaType multipartFormData = Objects.requireNonNull(MediaType.MULTIPART_FORM_DATA);
        
        mockMvc.perform(MockMvcRequestBuilders.multipart("/api/movies/load")
                        .file(file)
                        .characterEncoding("UTF-8")
                        .contentType(multipartFormData))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.total").value(2));

        long after = movieRepository.count();
        assertThat(after).isEqualTo(2);
    }
}
