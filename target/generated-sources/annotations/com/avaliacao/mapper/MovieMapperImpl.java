package com.avaliacao.mapper;

import com.avaliacao.dto.MovieResponse;
import com.avaliacao.model.Movie;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-06T15:40:50-0300",
    comments = "version: 1.5.5.Final, compiler: javac, environment: Java 17.0.8 (Eclipse Adoptium)"
)
@Component
public class MovieMapperImpl implements MovieMapper {

    @Override
    public MovieResponse toDto(Movie entity) {
        if ( entity == null ) {
            return null;
        }

        MovieResponse movieResponse = new MovieResponse();

        movieResponse.setId( entity.getId() );
        movieResponse.setYear( entity.getYear() );
        movieResponse.setTitle( entity.getTitle() );
        movieResponse.setStudios( entity.getStudios() );
        movieResponse.setProducers( entity.getProducers() );
        movieResponse.setWinner( entity.getWinner() );

        return movieResponse;
    }
}
