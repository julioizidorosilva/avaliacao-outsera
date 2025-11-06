package com.avaliacao.controller;

import com.avaliacao.model.Movie;
import com.avaliacao.dto.MovieResponse;
import com.avaliacao.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import com.avaliacao.dto.ProducerIntervalResponse;

import java.util.List;
import java.util.Optional;
import lombok.Data;
import lombok.NoArgsConstructor;

@Tag(name = "Filmes", description = "Operações relacionadas aos filmes do Golden Raspberry Awards")
@RestController
@RequestMapping("/api/movies")
@CrossOrigin(origins = "*")
public class MovieController {

    @Autowired
    private MovieService movieService;

    @Operation(summary = "Listar todos os filmes", 
               description = "Retorna uma lista completa de todos os filmes cadastrados, ordenados por ano")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de filmes retornada com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = MovieResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping
    public ResponseEntity<List<MovieResponse>> listarTodos() {
        try {
            List<Movie> movies = movieService.listarTodos();
            List<MovieResponse> resp = movies.stream().map(MovieResponse::new).collect(java.util.stream.Collectors.toList());
            return ResponseEntity.ok(resp);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @Operation(summary = "Buscar filme por ID", 
               description = "Retorna um filme específico baseado no ID fornecido")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Filme encontrado com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = MovieResponse.class))),
        @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor")
    })
    @GetMapping("/{id:[0-9]+}")
    public ResponseEntity<MovieResponse> buscarPorId(
            @Parameter(description = "ID único do filme", required = true, example = "1")
            @PathVariable Long id) {
        try {
            Optional<Movie> movie = movieService.buscarPorId(id);
            
            if (movie.isPresent()) {
                return ResponseEntity.ok(new MovieResponse(movie.get()));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    
    @Operation(summary = "Obter intervalos de prêmios dos produtores", 
               description = "Retorna os intervalos mínimo e máximo entre dois prêmios consecutivos dos produtores. " +
                           "Inclui o produtor com menor intervalo e o produtor com maior intervalo entre prêmios consecutivos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Intervalos calculados com sucesso",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ProducerIntervalResponse.class))),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ErrorResponse.class)))
    })
    @GetMapping("/producers/intervals")
    public ResponseEntity<?> obterIntervalosProducers() {
        try {
            return ResponseEntity.ok(movieService.calcularIntervalosProducers());
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("Erro interno do servidor: " + e.getMessage()));
        }
    }

    @Operation(summary = "Upload e carregamento de arquivo CSV", 
               description = "Faz upload de um arquivo CSV contendo dados de filmes e os carrega no banco de dados. " +
                           "O arquivo deve estar no formato CSV com as colunas: year, title, studios, producers, winner.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Arquivo processado com sucesso",
                    content = @Content(mediaType = "application/json")),
        @ApiResponse(responseCode = "400", description = "Arquivo não fornecido ou inválido"),
        @ApiResponse(responseCode = "500", description = "Erro interno do servidor",
                    content = @Content(mediaType = "application/json", 
                                     schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping(value = "/load", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadCsvAndLoad(
            @Parameter(description = "Arquivo CSV com dados dos filmes", required = true)
            @RequestParam("file") MultipartFile file) {
        if (file == null || file.isEmpty()) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Arquivo CSV não fornecido ou está vazio"));
        }

        try {
            int total = movieService.loadMoviesFromCsv(file.getInputStream());

            java.util.Map<String, Object> resp = new java.util.HashMap<>();
            resp.put("total", total);
            return ResponseEntity.status(HttpStatus.CREATED).body(resp);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("Erro ao processar CSV: " + e.getMessage()));
        }
    }

  
    @Data
    @NoArgsConstructor
    public static class ErrorResponse {
        private String erro;
        private long timestamp = System.currentTimeMillis();

        public ErrorResponse(String erro) {
            this.erro = erro;
            this.timestamp = System.currentTimeMillis();
        }
    }
}