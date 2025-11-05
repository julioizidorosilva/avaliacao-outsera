package com.avaliacao.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;
import lombok.NoArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;


@Tag(name = "API Resources", description = "Endpoints para descoberta e informações da API")
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ApiResourceController {

    @Operation(summary = "Descobrir recursos da API", 
               description = "Endpoint de descoberta da API que fornece informações sobre todos os recursos disponíveis, " +
                           "seguindo o Nível 2 de Richardson com recursos bem definidos e URIs específicas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Recursos da API retornados com sucesso",
                    content = @Content(mediaType = "application/json"))
    })
    @GetMapping
    public ResponseEntity<Map<String, Object>> getApiResources() {
        Map<String, Object> apiData = new HashMap<>();
        
        Map<String, Object> apiInfo = new HashMap<>();
        apiInfo.put("name", "Outsera API");
        apiInfo.put("version", "1.0");
        apiInfo.put("description", "API RESTful seguindo Nível 2 de Richardson");
        apiInfo.put("baseUrl", "/api");
        
        Map<String, Object> resources = new HashMap<>();
        
        Map<String, Object> movies = new HashMap<>();
        movies.put("description", "Gerenciamento de filmes (Golden Raspberry Awards)");
        movies.put("path", "/api/movies");
        movies.put("methods", Arrays.asList("GET", "POST", "HEAD", "OPTIONS"));
        movies.put("endpoints", Arrays.asList(
            "POST /api/movies/load - Carrega filmes do CSV",
            "GET /api/movies - Lista todos os filmes",
            "GET /api/movies/{id} - Busca filme por ID",
            "GET /api/movies/producers/intervals - Intervalos entre prêmios dos produtores"
        ));
        
        resources.put("movies", movies);
        
        apiData.put("api", apiInfo);
        apiData.put("resources", resources);
        
        return ResponseEntity.ok(apiData);
    }    
    
    @GetMapping("/health")
    public ResponseEntity<HealthResponse> healthCheck() {
        HealthResponse health = new HealthResponse();
        health.setStatus("UP");
        health.setTimestamp(System.currentTimeMillis());
        
        Map<String, Object> detalhes = new HashMap<>();
        detalhes.put("database", "H2");
        detalhes.put("jvm", System.getProperty("java.version"));
        detalhes.put("memoria_livre", Runtime.getRuntime().freeMemory() / 1024 / 1024 + " MB");
        health.setDetalhes(detalhes);
        
        return ResponseEntity.ok(health);
    }

  
    @GetMapping("/status-codes")
    public ResponseEntity<StatusCodesResponse> getStatusCodes() {
        StatusCodesResponse response = new StatusCodesResponse();
        
        Map<String, String> codigos = new HashMap<>();
        codigos.put("200", "OK - Operação bem-sucedida");
        codigos.put("201", "Created - Recurso criado com sucesso");
        codigos.put("204", "No Content - Operação bem-sucedida sem conteúdo");
        codigos.put("400", "Bad Request - Dados inválidos ou malformados");
        codigos.put("404", "Not Found - Recurso não encontrado");
        codigos.put("500", "Internal Server Error - Erro interno do servidor");
        
        response.setCodigosStatus(codigos);
        response.setDescricao("Códigos de status HTTP utilizados pela API conforme Nível 2 de Richardson");
        
        return ResponseEntity.ok(response);
    }

   
    @RequestMapping(method = RequestMethod.OPTIONS)
    public ResponseEntity<?> options() {
        return ResponseEntity.ok()
            .header("Allow", "GET, OPTIONS")
            .build();
    }

   
    
    @Data
    @NoArgsConstructor
    public static class ApiInfoResponse {
        private String nome;
        private String versao;
        private String descricao;
        private int nivelRichardson;
        private Map<String, String> recursos;
        private Map<String, String[]> metodosHttp;
    }

    @Data
    @NoArgsConstructor
    public static class HealthResponse {
        private String status;
        private long timestamp;
        private Map<String, Object> detalhes;
    }

    @Data
    @NoArgsConstructor
    public static class StatusCodesResponse {
        private String descricao;
        private Map<String, String> codigosStatus;
    }
}