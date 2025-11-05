package com.avaliacao.config;

import com.avaliacao.service.MovieService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

/**
 * Componente responsável por inicializar dados na aplicação
 * Carrega automaticamente os filmes do CSV ao iniciar
 */
@Component
// Só executa o carregamento automático se a propriedade 'app.load-on-startup' estiver definida como true
@ConditionalOnProperty(prefix = "app", name = "load-on-startup", havingValue = "true", matchIfMissing = false)
public class DataInitializer implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataInitializer.class);

    @Autowired
    private MovieService movieService;

    @Override
    public void run(String... args) throws Exception {
        try {
            logger.info("Iniciando carregamento de dados do CSV...");
            
            
            long existingMovies = movieService.contarFilmes();
            
            if (existingMovies == 0) {
                logger.info("Nenhum filme encontrado no banco. Carregando do CSV...");
                movieService.loadMoviesFromCsv();
                
                long totalMovies = movieService.contarFilmes();
              
                
                logger.info("Dados carregados com sucesso!");
                logger.info("Total de filmes: {}", totalMovies);
                
            } else {
                logger.info("Dados já existem no banco ({} filmes). Pulando carregamento do CSV.", existingMovies);
            }
            
        } catch (Exception e) {
            logger.error("Erro ao carregar dados do CSV: {}", e.getMessage());
            logger.info("A aplicação continuará funcionando, mas você pode carregar os dados manualmente via POST /api/movies/load");
        }
    }
}