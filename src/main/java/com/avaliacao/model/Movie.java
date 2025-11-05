package com.avaliacao.model;

import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Table(name = "movies")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Movie {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    @Column(name = "movie_year", nullable = false)
    private Integer year;

    @Column(nullable = false, length = 500)
    @ToString.Include
    private String title;

    @Column(length = 500)
    private String studios;

    @Column(length = 500)
    private String producers;

    @Column(name = "winner_flag")
    private Boolean winner = false;

    public Movie(Integer year, String title, String studios, String producers, Boolean winner) {
        this.year = year;
        this.title = title;
        this.studios = studios;
        this.producers = producers;
        this.winner = winner != null ? winner : Boolean.FALSE;
    }
}