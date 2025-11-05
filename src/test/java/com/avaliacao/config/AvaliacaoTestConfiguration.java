package com.avaliacao.config;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.ComponentScan;

@TestConfiguration
@ComponentScan(basePackages = {
    "com.avaliacao", 
    "com.avaliacao.mapper"  
})
public class AvaliacaoTestConfiguration {
    // Configuração específica para testes se necessária
}