package com.avaliacao.mapper;

import org.mapstruct.Mapper;
import com.avaliacao.model.Movie;
import com.avaliacao.dto.MovieResponse;

@Mapper(componentModel = "spring")
public interface MovieMapper {

    // Mapeia entidade para DTO de resposta (útil para retornar dados ao cliente).
    MovieResponse toDto(Movie entity);
}
