package com.avaliacao.mapper;

import com.avaliacao.dto.MovieResponse;
import com.avaliacao.model.Movie;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-05T18:05:24-0300",
    comments = "version: 1.5.5.Final, compiler: Eclipse JDT (IDE) 3.44.0.v20251023-0518, environment: Java 21.0.8 (Eclipse Adoptium)"
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
        movieResponse.setProducers( entity.getProducers() );
        movieResponse.setStudios( entity.getStudios() );
        movieResponse.setTitle( entity.getTitle() );
        movieResponse.setWinner( entity.getWinner() );
        movieResponse.setYear( entity.getYear() );

        return movieResponse;
    }
}
