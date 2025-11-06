package com.avaliacao.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.EnabledIfSystemProperty;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@EnabledIfSystemProperty(named = "run.integration.tests", matches = "true")
public class CsvValidationTest {

    private final RestTemplate restTemplate = new RestTemplate();

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void validateApiDataAgainstCsvFile() throws Exception {
        System.out.println("\n=== VALIDAÇÃO: API vs CSV ===");
        
        // ===== ETAPA 0: NÃO carregar dados no H2 - apenas testar contra API rodando =====
        System.out.println("ETAPA 0: Verificando aplicação em produção (SEM carregar dados no H2)...");
        
        try {
            ResponseEntity<String> apiCheck = restTemplate.getForEntity("http://localhost:8080/api/movies/producers/intervals", String.class);
            System.out.println("Aplicação em produção está respondendo: " + apiCheck.getStatusCode());
        } catch (Exception e) {
            System.out.println("FALHA: Aplicação em produção não está acessível!");
            System.out.println("   Execute: mvn spring-boot:run em outro terminal");
            System.out.println("   Aguarde a mensagem 'Started AvaliacaoApplication'");
            throw new RuntimeException("Aplicação não está rodando em http://localhost:8080", e);
        }
        
  
        // ===== ETAPA 1: Ler dados do CSV do classpath =====
        System.out.println("ETAPA 1: Lendo dados do arquivo CSV do classpath...");
        
        InputStream csvInputStream = new ClassPathResource("movielist.csv").getInputStream();
        assertThat(csvInputStream).as("Arquivo movielist.csv deve estar no classpath").isNotNull();
        
        Map<String, List<Integer>> csvProducerWins = new HashMap<>();
        
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(csvInputStream, StandardCharsets.UTF_8))) {
            String line = reader.readLine(); 
            
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(";");
                
                if (parts.length >= 5 && "yes".equalsIgnoreCase(parts[4].trim())) {
                    String year = parts[0].trim();
                    String producers = parts[3].trim();
                    
                    int yearInt = Integer.parseInt(year);
                    
                    String[] producerArray = producers.split(" and |, and |, ");
                    for (String producer : producerArray) {
                        producer = producer.trim();
                        if (!producer.isEmpty()) {
                            csvProducerWins.computeIfAbsent(producer, k -> new ArrayList<>()).add(yearInt);
                        }
                    }
                }
            }
        }
        
        csvProducerWins.values().forEach(Collections::sort);
        
        System.out.println("Produtores vencedores encontrados no CSV: " + csvProducerWins.size());
        
        // ===== ETAPA 2: Calcular intervalos esperados =====
        System.out.println("ETAPA 2: Calculando intervalos esperados...");
        Map<String, Object> expectedIntervals = calculateExpectedIntervals(csvProducerWins);
        
        // ===== ETAPA 3: Fazer requisição HTTP para aplicação em produção =====
        System.out.println("ETAPA 3: Consultando aplicação em produção...");
        
        ResponseEntity<String> response = restTemplate.getForEntity("http://localhost:8080/api/movies/producers/intervals", String.class);
        
        assertThat(response.getStatusCode().value()).as("Status HTTP deve ser 200").isEqualTo(200);
        assertThat(response.getBody()).as("Response body não deve estar vazio").isNotNull();
        
        // ===== ETAPA 4: Comparar resultados (normalizar tipos e ignorar ordem) =====
        System.out.println("ETAPA 4: Comparando resultados...");
        
        TypeReference<Map<String, Object>> typeRef = new TypeReference<Map<String, Object>>() {};
        Map<String, Object> actualResponse = objectMapper.readValue(response.getBody(), typeRef);
        
        compareIntervalResults(expectedIntervals, actualResponse);
        
        System.out.println("=== VALIDAÇÃO CONCLUÍDA COM SUCESSO ===");
    }

    private Map<String, Object> calculateExpectedIntervals(Map<String, List<Integer>> producerWins) {
        List<Map<String, Object>> allIntervals = new ArrayList<>();
        
        for (Map.Entry<String, List<Integer>> entry : producerWins.entrySet()) {
            String producer = entry.getKey();
            List<Integer> years = entry.getValue();
            
            if (years.size() >= 2) {
                for (int i = 0; i < years.size() - 1; i++) {
                    int previousWin = years.get(i);
                    int followingWin = years.get(i + 1);
                    int interval = followingWin - previousWin;
                    
                    Map<String, Object> intervalData = new HashMap<>();
                    intervalData.put("producer", producer);
                    intervalData.put("interval", interval);
                    intervalData.put("previousWin", previousWin);
                    intervalData.put("followingWin", followingWin);
                    
                    allIntervals.add(intervalData);
                }
            }
        }
        
        if (allIntervals.isEmpty()) {
            return Map.of("min", List.of(), "max", List.of());
        }
        
        int minInterval = allIntervals.stream().mapToInt(interval -> (Integer) interval.get("interval")).min().orElse(0);
        int maxInterval = allIntervals.stream().mapToInt(interval -> (Integer) interval.get("interval")).max().orElse(0);
        
        List<Map<String, Object>> minIntervals = allIntervals.stream()
                .filter(interval -> interval.get("interval").equals(minInterval))
                .collect(Collectors.toList());
                
        List<Map<String, Object>> maxIntervals = allIntervals.stream()
                .filter(interval -> interval.get("interval").equals(maxInterval))
                .collect(Collectors.toList());
        
        return Map.of("min", minIntervals, "max", maxIntervals);
    }

    private void compareIntervalResults(Map<String, Object> expected, Map<String, Object> actual) {
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> expectedMin = (List<Map<String, Object>>) expected.get("min");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> expectedMax = (List<Map<String, Object>>) expected.get("max");
        
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> actualMin = (List<Map<String, Object>>) actual.get("min");
        @SuppressWarnings("unchecked")
        List<Map<String, Object>> actualMax = (List<Map<String, Object>>) actual.get("max");
        
        normalizeNumericTypes(expectedMin);
        normalizeNumericTypes(expectedMax);
        normalizeNumericTypes(actualMin);
        normalizeNumericTypes(actualMax);
        
        assertThat(actualMin).as("Número de intervalos mínimos deve ser igual").hasSameSizeAs(expectedMin);
        assertThat(actualMax).as("Número de intervalos máximos deve ser igual").hasSameSizeAs(expectedMax);
        
        Set<Map<String, Object>> expectedMinSet = new HashSet<>(expectedMin);
        Set<Map<String, Object>> expectedMaxSet = new HashSet<>(expectedMax);
        Set<Map<String, Object>> actualMinSet = new HashSet<>(actualMin);
        Set<Map<String, Object>> actualMaxSet = new HashSet<>(actualMax);
        
        assertThat(actualMinSet).as("Intervalos mínimos devem ser iguais (ignorando ordem)").isEqualTo(expectedMinSet);
        assertThat(actualMaxSet).as("Intervalos máximos devem ser iguais (ignorando ordem)").isEqualTo(expectedMaxSet);
        
        System.out.println("Todos os intervalos correspondem entre CSV e API!");
    }

    private void normalizeNumericTypes(List<Map<String, Object>> intervals) {
        for (Map<String, Object> interval : intervals) {
            for (Map.Entry<String, Object> entry : interval.entrySet()) {
                Object value = entry.getValue();
                if (value instanceof Number && !(value instanceof Integer)) {
                    entry.setValue(((Number) value).intValue());
                }
            }
        }
    }
}
