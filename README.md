# Avaliacao API - Golden Raspberry Awards

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.0-green)
![Maven](https://img.shields.io/badge/Maven-3.6+-blue)
![H2 Database](https://img.shields.io/badge/H2-In--Memory-lightgrey)
![License](https://img.shields.io/badge/License-MIT-yellow)

API RESTful moderna construída com Spring Boot para análise de filmes vencedores do Golden Raspberry Awards (Razzie). 
Implementa o **Nível 2 de Maturidade de Richardson** com recursos bem definidos, métodos HTTP apropriados e códigos de status corretos.

## Funcionalidades

-  **Gestão de Filmes** - CRUD completo via REST API
-  **Análise de Produtores** - Cálculo de intervalos entre prêmios consecutivos  
-  **Upload CSV** - Carregamento de dados via arquivo
-  **Health Check** - Monitoramento da aplicação
-  **Logs Estruturados** - JSON logs com correlação de requisições
-  **Documentação Swagger** - OpenAPI 3.0 com interface interativa
-  **Collection Postman** - Testes manuais e automação
-  **Testes Automatizados** - Cobertura de integração

## Stack Tecnológica

### Core Framework
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Java** | 17 | Linguagem de programação |
| **Spring Boot** | 3.2.0 | Framework principal |
| **Spring Web** | 3.2.0 | REST API e servidor embutido |
| **Spring Data JPA** | 3.2.0 | Persistência e ORM |

### Database & Storage  
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **H2 Database** | 2.x | Banco em memória para dev/test |
| **Hibernate** | 6.3.1 | ORM e mapeamento objeto-relacional |

### Desenvolvimento & Build
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Maven** | 3.11.0 | Gerenciamento de dependências |
| **Lombok** | 1.18.26 | Redução de boilerplate |
| **MapStruct** | 1.5.5 | Mapeamento entre DTOs e entidades |

### Observabilidade & Logs
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **Logback** | - | Sistema de logging |
| **Logstash Encoder** | 7.4 | Logs estruturados em JSON |

### Documentação
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **SpringDoc OpenAPI** | 2.2.0 | Documentação interativa da API |
| **Swagger UI** | - | Interface visual para testes |

### Testes & Qualidade
| Tecnologia | Versão | Descrição |
|------------|--------|-----------|
| **JUnit 5** | - | Framework de testes |
| **Spring Boot Test** | 3.2.0 | Testes de integração |
| **MockMvc** | - | Testes de controllers |

## Início Rápido

### Pré-requisitos
```bash
# Verificar instalações necessárias
java -version    # Java 17+
mvn -version     # Maven 3.6+
```

### Clonar e Compilar
```bash
git clone https://github.com/julioizidorosilva/avaliacao-outsera.git
cd avaliacao-outsera
mvn clean compile

### Executar Aplicação
```bash
# Desenvolvimento - com auto-reload
mvn spring-boot:run

# Produção - JAR executável
mvn package
java -jar target/avaliacao-1.0-SNAPSHOT.jar
```

### Verificar Status
```bash
# Health check
curl http://localhost:8080/api/health

# Descobrir recursos disponíveis
curl http://localhost:8080/api
```

## Documentação da API

A API possui documentação interativa completa gerada automaticamente com **Swagger/OpenAPI 3.0**.

### Acessar Swagger UI
Após iniciar a aplicação, acesse:

```
Interface Visual: http://localhost:8080/swagger-ui.html
Especificação JSON: http://localhost:8080/v3/api-docs
Especificação YAML: http://localhost:8080/v3/api-docs.yaml
```

### Recursos da Documentação
- **Teste Interativo** - Execute chamadas diretamente na interface
- **Esquemas Detalhados** - Documentação completa dos DTOs
- **Exemplos de Resposta** - Samples para cada endpoint
- **Códigos de Status** - Documentação de todos os códigos HTTP
- **Parâmetros** - Descrição detalhada de cada parâmetro
- **Autenticação** - Documentação de segurança (quando aplicável)

### Screenshots
```bash
# Verificar se a documentação está ativa
curl -s http://localhost:8080/v3/api-docs | jq '.info.title'
# Output: "Golden Raspberry Awards API"
```

## API Endpoints

### **Descoberta & Monitoramento**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api` | Recursos disponíveis na API |
| `GET` | `/api/health` | Status da aplicação |
| `GET` | `/api/status-codes` | Documentação dos códigos HTTP |
| `OPTIONS` | `/api` | Verificação CORS |

### 🎬 **Gestão de Filmes**
| Método | Endpoint | Descrição |
|--------|----------|-----------|
| `GET` | `/api/movies` | Lista todos os filmes |
| `GET` | `/api/movies/{id}` | Busca filme por ID |
| `GET` | `/api/movies/producers/intervals` | Intervalos entre prêmios |
| `POST` | `/api/movies/load` | Upload CSV (multipart) |

### **Exemplos de Resposta**

<details>
<summary>GET /api/movies/producers/intervals</summary>

```json
{
  "min": [
    {
      "producer": "Joel Silver",
      "interval": 1,
      "previousWin": 1990,
      "followingWin": 1991
    }
  ],
  "max": [
    {
      "producer": "Matthew Vaughn", 
      "interval": 13,
      "previousWin": 2002,
      "followingWin": 2015
    }
  ]
}
```
</details>

## Executando Testes

### Testes Rápidos
```bash
# Todos os testes
mvn test

# Apenas testes de integração
mvn test -Dtest="*IntegrationTest"

# Teste específico
mvn test -Dtest="MovieControllerIntegrationTest"
```

### Testes com Debug
```bash
# Com stacktrace completo
mvn test -DtrimStackTrace=false

# Teste específico com debug
mvn test -Dtest="MovieControllerIntegrationTest#listAllMovies_andFavicon"

## Estrutura do Projeto

```
src/
├── main/
│   ├── java/com/avaliacao/
│   │   ├── AvaliacaoApplication.java        # Classe principal 
│   │   ├── controller
│   │   │   ├── ApiResourceController.java   # API discovery & health
│   │   │   ├── MovieController.java         # REST Controller filmes
│   │   │   └── GlobalExceptionHandler.java  # Tratamento de erros
│   │   ├── model/
│   │   │   └── Movie.java                   # Entidade JPA
│   │   ├── repository/
│   │   │   └── MovieRepository.java         # Acesso a dados
│   │   ├── service/
│   │   │   └── MovieService.java            # Lógica de negócio
│   │   ├── dto/
│   │   │   ├── MovieResponse.java           # DTOs de resposta
│   │   │   └── ProducerIntervalResponse.java
│   │   ├── mapper/
│   │   │   └── MovieMapper.java             # MapStruct mappers
│   │   └── logging/
│   │       └── RequestIdFilter.java         # Correlação de logs
│   └── resources/
│       ├── application.properties          # Configurações
│       ├── logback-spring.xml              # Config de logs
│       ├── movielist.csv                   # Dados de exemplo
│       └── static/
│           └── index.html                  # Página inicial
└── test/
    └── java/com/avaliacao/
        └── controller/
            ├── MovieControllerIntegrationTest.java  # Testes integração
            └── MovieUploadIntegrationTest.java      # Testes upload
```

## Collection Postman

Utilize a collection completa disponível em `postman_collection/`:

```bash
postman_collection/
├── movies-api.postman_collection.json    # Collection completa
├── movies-api.postman_environment.json   # Environment local  
└── README.md                             # Instruções detalhadas
```

### Importar no Postman:
1. **Import** → `movies-api.postman_collection.json`
2. **Import** → `movies-api.postman_environment.json`  
3. **Selecionar Environment** → "Avaliacao API - Local"
4. **Executar testes** na ordem sugerida

## Configuração & Customização

### Variáveis de Ambiente
| Variável | Padrão | Descrição |
|----------|---------|-----------|
| `server.port` | `8080` | Porta da aplicação |
| `app.load-on-startup` | `false` | Auto-carregamento do CSV |

### Carregamento Automático de Dados
```bash
# Via properties
echo "app.load-on-startup=true" >> src/main/resources/application.properties

# Via JVM argument
mvn spring-boot:run -Dapp.load-on-startup=true
```

### Console H2 (Desenvolvimento)
- **URL**: http://localhost:8080/h2-console
- **JDBC URL**: `jdbc:h2:mem:testdb`
- **User**: `sa`
- **Password**: *(vazio)*

## Troubleshooting

### Problemas Comuns

| Problema | Solução |
|----------|---------|
| **Porta 8080 ocupada** | Altere `server.port` em `application.properties` |
| **Erro de compilação MapStruct** | Execute `mvn clean compile` |
| **Testes falhando** | Verifique se não há aplicação rodando na porta 8080 |
| **JAR não executa** | Verifique Java 17+ com `java -version` |

### Logs de Debug
```bash
# Ativar logs de debug
mvn spring-boot:run -Dlogging.level.com.avaliacao=DEBUG

# Ver SQL queries
mvn spring-boot:run -Dlogging.level.org.hibernate.SQL=DEBUG
```

## Recursos Implementados 

### Documentação Swagger/OpenAPI
A aplicação agora possui documentação interativa completa:

- **Swagger UI**: Interface visual acessível em `/swagger-ui.html`
- **Especificação OpenAPI 3.0**: Disponível em `/v3/api-docs` (JSON) e `/v3/api-docs.yaml` (YAML)
- **Anotações Completas**: Todos os controllers possuem anotações `@Operation`, `@ApiResponses` e `@Parameter`
- **Esquemas DTOs**: Documentação automática de todos os modelos de dados
- **Testes Interativos**: Execute chamadas diretamente na interface Swagger
- **Metadados API**: Informações sobre contato, licença e versão

### Configuração Implementada
- **SpringDoc OpenAPI 2.2.0**: Biblioteca para geração automática da documentação
- **OpenApiConfig.java**: Configuração centralizada com informações da API
- **Tags por Controller**: Organização lógica dos endpoints
- **Responses HTTP**: Documentação completa dos códigos de status
- **Content Types**: Especificação precisa dos tipos de mídia aceitos

### Como Testar a Documentação
```bash
# 1. Iniciar aplicação
mvn spring-boot:run

# 2. Acessar Swagger UI
open http://localhost:8080/swagger-ui.html

# 3. Verificar especificação OpenAPI
curl http://localhost:8080/v3/api-docs | jq '.info.title'
# Output: "Golden Raspberry Awards API"
```


