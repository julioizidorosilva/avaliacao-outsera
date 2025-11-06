package com.avaliacao.service;

import com.avaliacao.dto.ProducerInterval;
import com.avaliacao.dto.ProducerIntervalResponse;
import com.avaliacao.model.Movie;
import com.avaliacao.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

 

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    private static final Logger logger = LoggerFactory.getLogger(MovieService.class);

    public void loadMoviesFromCsv() throws IOException {
        if (movieRepository.count() > 0) {
            logger.info("Filmes já carregados no banco de dados");
            return;
        }

        List<Movie> movies = new ArrayList<>();
        
        try {
            ClassPathResource resource = new ClassPathResource("movielist.csv");
            BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()));
            
            String line;
            boolean isHeader = true;
            
            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }
                
                String[] fields = line.split(";");
                
                if (fields.length >= 4) { 
                    try {
                        Integer year = Integer.parseInt(fields[0].trim());
                        String title = fields[1].trim();
                        String studios = fields[2].trim();
                        String producers = fields[3].trim();
                        
                        Boolean winner = false;
                        if (fields.length > 4 && !fields[4].trim().isEmpty()) {
                            winner = "yes".equalsIgnoreCase(fields[4].trim());
                        }
                        
                        Movie movie = new Movie(year, title, studios, producers, winner);
                        movies.add(movie);
                        
                    } catch (NumberFormatException e) {
                        logger.error("Erro ao processar linha: {} - causa: {}", line, e.getMessage());
                    }
                }
            }
            
            reader.close();
            
        } catch (IOException e) {
            throw new IOException("Erro ao ler arquivo CSV: " + e.getMessage());
        }
        
        movieRepository.saveAll(movies);
        logger.info("Carregados {} filmes do arquivo CSV", movies.size());
    }

    @Transactional
    public int loadMoviesFromCsv(java.io.InputStream input) throws IOException {
        List<Movie> movies = new ArrayList<>();

        try (java.io.BufferedReader reader = new java.io.BufferedReader(new java.io.InputStreamReader(input))) {
            String line;
            boolean isHeader = true;

            while ((line = reader.readLine()) != null) {
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] fields = line.split(";");

                if (fields.length >= 4) { // Mudado de >= 5 para >= 4 para aceitar linhas sem campo winner
                    try {
                        Integer year = Integer.parseInt(fields[0].trim());
                        String title = fields[1].trim();
                        String studios = fields[2].trim();
                        String producers = fields[3].trim();
                        
                        Boolean winner = false;
                        if (fields.length > 4 && !fields[4].trim().isEmpty()) {
                            winner = "yes".equalsIgnoreCase(fields[4].trim());
                        }

                        Movie movie = new Movie(year, title, studios, producers, winner);
                        movies.add(movie);

                    } catch (NumberFormatException e) {
                        logger.error("Erro ao processar linha: {} - causa: {}", line, e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            throw new IOException("Erro ao ler CSV enviado: " + e.getMessage());
        }

        movieRepository.deleteAll();
        movieRepository.saveAll(movies);
        logger.info("Sobrescritos e carregados {} filmes a partir do upload CSV", movies.size());
        return movies.size();
    }

   
    public List<Movie> listarTodos() {
        return movieRepository.findAllByOrderByYearAsc();
    }



    public Optional<Movie> buscarPorId(Long id) {
        if (id == null) {
            return Optional.empty();
        }
        return movieRepository.findById(id);
    }

  
    public long contarFilmes() {
        return movieRepository.count();
    }

    public ProducerIntervalResponse calcularIntervalosProducers() {
        List<Movie> winners = movieRepository.findByWinnerTrueOrderByYearAsc();
        
        Map<String, List<Integer>> producerWins = new HashMap<>();
        
        for (Movie movie : winners) {
            String producers = movie.getProducers();
            if (producers != null && !producers.trim().isEmpty()) {
                String[] producerArray = splitProducers(producers);
                
                for (String producer : producerArray) {
                    producer = producer.trim();
                    if (!producer.isEmpty()) {
                        producerWins.computeIfAbsent(producer, k -> new ArrayList<>()).add(movie.getYear());
                    }
                }
            }
        }
        
        List<ProducerInterval> minIntervals = new ArrayList<>();
        List<ProducerInterval> maxIntervals = new ArrayList<>();
        int minInterval = Integer.MAX_VALUE;
        int maxInterval = Integer.MIN_VALUE;
        
        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            
            if (years.size() <= 1) continue;
            
            years.sort(Integer::compareTo);
            
            for (int i = 1; i < years.size(); i++) {
                int previousWin = years.get(i - 1);
                int followingWin = years.get(i);
                int interval = followingWin - previousWin;
                
                ProducerInterval producerInterval = new ProducerInterval(producer, interval, previousWin, followingWin);
                
                if (interval < minInterval) {
                    minInterval = interval;
                    minIntervals.clear();
                    minIntervals.add(producerInterval);
                } else if (interval == minInterval) {
                    minIntervals.add(producerInterval);
                }
                
                if (interval > maxInterval) {
                    maxInterval = interval;
                    maxIntervals.clear();
                    maxIntervals.add(producerInterval);
                } else if (interval == maxInterval) {
                    maxIntervals.add(producerInterval);
                }
            }
        }
        
        return new ProducerIntervalResponse(minIntervals, maxIntervals);
    }
    
    private String[] splitProducers(String producers) {
        producers = producers.replaceAll("\\s+and\\s+", ",")
                           .replaceAll("\\s*,\\s*", ",")
                           .replaceAll("\\s*;\\s*", ",");
        
        return producers.split(",");
    }
}