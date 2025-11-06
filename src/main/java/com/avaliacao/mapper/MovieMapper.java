package com.avaliacao.mapper;

import org.mapstruct.Mapper;
import com.avaliacao.model.Movie;
import com.avaliacao.dto.MovieResponse;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    MovieResponse toDto(Movie entity);
}
