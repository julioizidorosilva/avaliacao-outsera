package com.avaliacao.dto;

import com.avaliacao.model.Movie;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MovieResponse {
    private Long id;
    private Integer year;
    private String title;
    private String studios;
    private String producers;
    private Boolean winner;

    /**
     * Construtor de conversão - substitui o MovieMapper
     * Converte entidade Movie para DTO MovieResponse
     */
    public MovieResponse(Movie movie) {
        this.id = movie.getId();
        this.year = movie.getYear();
        this.title = movie.getTitle();
        this.studios = movie.getStudios();
        this.producers = movie.getProducers();
        this.winner = movie.getWinner();
    }

    /**
     * Método estático de conveniência para conversão
     */
    public static MovieResponse fromEntity(Movie movie) {
        return new MovieResponse(movie);
    }
}
