package com.avaliacao.repository;

import com.avaliacao.model.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MovieRepository extends JpaRepository<Movie, Long> {

    List<Movie> findAllByOrderByYearAsc();

    List<Movie> findByWinnerTrue();

    List<Movie> findByWinnerTrueOrderByYearAsc();

    @Query("SELECT m FROM Movie m WHERE m.winner = true AND m.year BETWEEN :startDecade AND :endDecade ORDER BY m.year")
    List<Movie> findWinnersByDecade(@Param("startDecade") Integer startDecade, @Param("endDecade") Integer endDecade);

    Long countByWinnerTrue();
}