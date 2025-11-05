## Coleção Postman — Avaliacao API (Completa)

Arquivo(s):
- `movies-api.postman_collection.json` — coleção completa (requests para todas as rotas da API)
- `movies-api.postman_environment.json` — environment com variáveis usadas pela coleção

## Resumo da Coleção

Esta collection contém **todos os endpoints disponíveis** na API Avaliacao, organizados em pastas:

### **API Discovery & Info**
- `GET /api` - Descoberta de recursos da API
- `GET /api/health` - Health check da aplicação
- `GET /api/status-codes` - Documentação dos códigos HTTP
- `OPTIONS /api` - Verificação CORS

### **Movies Operations**  
- `POST /api/movies/load` - Upload de arquivo CSV
- `GET /api/movies` - Lista todos os filmes
- `GET /api/movies/{id}` - Busca filme por ID
- `GET /api/movies/producers/intervals` - Intervalos entre prêmios dos produtores
- `OPTIONS /api/movies` - Verificação CORS para movies

###  **Error Testing**
- Testes com IDs inválidos
- Upload sem arquivo
- Cenários de erro diversos

##  Passos para Usar

1. **Abra o Postman**
2. **Import Collection:** `postman_collection/movies-api.postman_collection.json`
3. **Import Environment:** `postman_collection/movies-api.postman_environment.json`
4. **Selecione Environment:** `Avaliacao API - Local` (canto superior direito)
5. **Verifique Variables:**
   - `baseUrl`: `http://localhost:8080` (altere se necessário)
   - `createdMovieId`: preenchido automaticamente pelos testes
6. **Execute os testes** na ordem sugerida

##  Ordem Recomendada de Execução

1. **API Discovery** → `GET /api` (verificar se API está rodando)
2. **Health Check** → `GET /api/health` (verificar status)
3. **Load CSV** → `POST /api/movies/load` (popular dados)
4. **List Movies** → `GET /api/movies` (listar e capturar ID)
5. **Get Movie by ID** → usar ID capturado automaticamente
6. **Producers Intervals** → calcular intervalos
7. **Error Tests** → testar cenários de erro

##  Funcionalidades Especiais

###  **Testes Automatizados**
Cada requisição inclui testes automáticos que verificam:
- Status codes corretos
- Estrutura da resposta
- Propriedades obrigatórias
- Tipos de dados

###  **Variáveis Dinâmicas**
- `createdMovieId` é preenchido automaticamente quando você lista os filmes
- Permite testar endpoints que dependem de IDs reais

###  **Upload de CSV**
- Requisição configurada para multipart/form-data
- Campo `file` preparado (você só precisa selecionar o arquivo CSV)

##  Exemplos CLI (Curl)

Se preferir testar via linha de comando:

```zsh
# Variável base
BASE_URL="http://localhost:8080"

# Descobrir recursos da API
curl -i "$BASE_URL/api"

# Health check
curl -i "$BASE_URL/api/health"

# Listar filmes
curl -i "$BASE_URL/api/movies"

# Buscar filme específico
curl -i "$BASE_URL/api/movies/1"

# Intervalos de produtores
curl -i "$BASE_URL/api/movies/producers/intervals"

# Upload de CSV (substitua o caminho do arquivo)
curl -v -X POST -F "file=@/caminho/para/movielist.csv" "$BASE_URL/api/movies/load"
```

##  Observações Importantes

- **Overwrite de dados**: O endpoint `POST /api/movies/load` **substitui** todos os dados existentes
- **Arquivo CSV**: Use o formato com separador `;` (ponto-e-vírgula) 
- **CORS habilitado**: API aceita requisições de qualquer origem
- **Environment variables**: A collection atualiza automaticamente as variáveis conforme necessário

##  Resultados Esperados

Todos os testes devem **passar** se a API estiver funcionando corretamente:
-  Status codes apropriados (200, 201, 404, 400)
-  Estruturas JSON válidas
-  Funcionalidades completas da aplicação

