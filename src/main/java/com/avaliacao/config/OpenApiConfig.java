package com.avaliacao.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuração do OpenAPI/Swagger para documentação interativa da API
 */
@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI avaliacaoOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Avaliacao API - Golden Raspberry Awards")
                        .description("API RESTful para análise de filmes vencedores do Golden Raspberry Awards (Razzie). " +
                                   "Implementa o Nível 2 de Maturidade de Richardson com recursos bem definidos, " +
                                   "métodos HTTP apropriados e códigos de status corretos.")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Equipe Avaliacao")
                                .email("contato@avaliacao.com")
                                .url("https://github.com/usuario/avaliacao"))
                        .license(new License()
                                .name("MIT License")
                                .url("https://opensource.org/licenses/MIT")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Servidor de Desenvolvimento"),
                        new Server()
                                .url("https://api.avaliacao.com")
                                .description("Servidor de Produção")
                ));
    }
}