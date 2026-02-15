# GNews API - Diagramas Arquiteturais

## 1. Diagrama Entidade-Relacionamento (ERD)

```mermaid
erDiagram
    SOURCE ||--o{ ARTICLE : "has"
    
    SOURCE {
        string id PK
        string name
        string url
        string country
    }
    
    ARTICLE {
        string id PK
        string title
        string description
        string content
        string url
        string image
        datetime publishedAt
        string lang
        string category
        string source_id FK
    }
```

### Descrição das Entidades

**Source (Fonte de Notícias)**
- **id**: Identificador único da fonte
- **name**: Nome da fonte (ex: BBC News, CNN)
- **url**: URL da página principal da fonte
- **country**: Código do país da fonte (ex: us, br, uk)

**Article (Artigo)**
- **id**: Identificador único do artigo
- **title**: Título do artigo
- **description**: Descrição resumida do artigo
- **content**: Conteúdo completo do artigo
- **url**: URL do artigo
- **image**: URL da imagem/thumbnail
- **publishedAt**: Data e hora de publicação
- **lang**: Código do idioma (ex: en, pt, es)
- **category**: Categoria interna para filtro (ex: breaking-news, world, nation, business, technology, entertainment, sports, science, health)
- **source_id**: Referência externa para a Source

### Relacionamento
- Uma **Source** pode ter múltiplos **Articles** (1:N)
- Um **Article** sempre pertence a uma única **Source**

---

## 2. Diagrama de Sequência - Fluxo de Requisição

### 2.1 Fluxo: GET /api/v4/top-headlines

```mermaid
sequenceDiagram
    participant Client
    participant ApiKeyInterceptor
    participant RateLimitService
    participant ArticleController
    participant ArticleService
    participant ArticleRepository
    participant DataStore

    Client->>ApiKeyInterceptor: GET /api/v4/top-headlines?apikey=XXX&q=tech&max=10
    
    note over ApiKeyInterceptor: 1. SECURITY LAYER
    ApiKeyInterceptor->>ApiKeyInterceptor: Valida presença de apikey
    alt API Key Inválida
        ApiKeyInterceptor-->>Client: 401 Unauthorized
    end
    
    ApiKeyInterceptor->>RateLimitService: checkRateLimit(apikey)
    note over RateLimitService: 2. RATE LIMIT CHECK
    RateLimitService->>RateLimitService: Verifica quota do cliente
    alt Limite Excedido
        RateLimitService-->>Client: 429 Too Many Requests
    end
    
    ApiKeyInterceptor->>ArticleController: Passa requisição
    
    note over ArticleController: 3. CONTROLLER LAYER
    ArticleController->>ArticleController: Parse & Validação de Parâmetros
    ArticleController->>ArticleService: getTopHeadlines(category, lang, country, q, page, max)
    
    note over ArticleService: 4. SERVICE LAYER
    ArticleService->>ArticleRepository: findAll()
    
    note over ArticleRepository: 5. REPOSITORY LAYER
    ArticleRepository->>DataStore: Recupera lista de articles em memória
    DataStore-->>ArticleRepository: List<Article>
    ArticleRepository-->>ArticleService: List<Article>
    
    note over ArticleService: 6. BUSINESS LOGIC
    ArticleService->>ArticleService: Aplica filtros (category, lang, country, keywords)
    ArticleService->>ArticleService: Ordena por publishedAt DESC
    ArticleService->>ArticleService: Aplica paginação (skip, limit)
    ArticleService->>ArticleService: Mapeia Article → ArticleDto
    
    ArticleService-->>ArticleController: ArticlesResponse(total, articles)
    
    ArticleController-->>Client: 200 OK + JSON Response
```

### 2.2 Fluxo: GET /api/v4/search

```mermaid
sequenceDiagram
    participant Client
    participant ApiKeyInterceptor
    participant RateLimitService
    participant ArticleController
    participant ArticleService
    participant ArticleRepository
    participant DataStore

    Client->>ApiKeyInterceptor: GET /api/v4/search?q=COVID&lang=en&country=us&apikey=XXX

    note over ApiKeyInterceptor: 1. SECURITY LAYER
    ApiKeyInterceptor->>ApiKeyInterceptor: Valida apikey required
    alt API Key Missing/Invalid
        ApiKeyInterceptor-->>Client: 401 Unauthorized
    end

    ApiKeyInterceptor->>RateLimitService: checkRateLimit(apikey)
    note over RateLimitService: 2. RATE LIMITING
    alt Quota Excedida
        RateLimitService-->>Client: 429 Too Many Requests
    else Dentro do Limite
        RateLimitService->>RateLimitService: Incrementa contador
    end

    ApiKeyInterceptor->>ArticleController: Passa requisição

    note over ArticleController: 3. CONTROLLER LAYER
    ArticleController->>ArticleController: Valida parâmetro 'q' (obrigatório em /search)
    ArticleController->>ArticleService: search(q, lang, country, sortBy, from, to, page, max)

    note over ArticleService: 4. SERVICE LAYER - PROCESSAMENTO
    ArticleService->>ArticleRepository: findAll()
    ArticleRepository->>DataStore: Acessa repositório em memória
    DataStore-->>ArticleRepository: List<Article> completo
    ArticleRepository-->>ArticleService: List<Article>

    note over ArticleService: 5. FILTERING & SORTING
    ArticleService->>ArticleService: Filter: q em title/description
    ArticleService->>ArticleService: Filter: lang
    ArticleService->>ArticleService: Filter: country (via source)
    ArticleService->>ArticleService: Filter: date range (from/to)
    ArticleService->>ArticleService: Sort: publishedAt DESC (ou relevance)
    ArticleService->>ArticleService: Pagination: skip=(page-1)*max, limit=min(max, 100)

    note over ArticleService: 6. DTO MAPPING
    ArticleService->>ArticleService: Converte Article → ArticleDto com SourceDto
    ArticleService->>ArticleService: Formata publishedAt para ISO 8601

    ArticleService-->>ArticleController: ArticlesResponse(total, dtos)
    ArticleController-->>Client: 200 OK { status, totalArticles, articles [...] }
```

---

## 3. Camadas da Aplicação

### Security Layer (Segurança)
- **ApiKeyInterceptor**: Valida a presença e validade da API key em cada requisição
- **Responsabilidade**: Rejeitar requisições não autorizadas com 401 Unauthorized

### Rate Limiting Layer
- **RateLimitService**: Controla quantas requisições cada cliente pode fazer
- **Responsabilidade**: Rejeitar com 429 Too Many Requests quando limite é excedido

### Controller Layer (Camada de Apresentação)
- **ArticleController**: Recebe requisições HTTP, valida parâmetros, orquestra a chamada ao Service
- **Endpoints**:
  - `GET /api/v4/top-headlines` - Notícias em destaque
  - `GET /api/v4/search` - Busca por palavra-chave
- **Responsabilidade**: Validação de entrada, chamada ao Service, formatação de resposta

### Service Layer (Lógica de Negócio)
- **ArticleService**: Implementa a lógica de filtering, sorting, paginação
- **Responsabilidade**:
  - Aplicar filtros (categoria, idioma, país, keywords, datas)
  - Ordenar resultados
  - Aplicar paginação
  - Mapear domínio para DTOs

### Repository Layer (Acesso a Dados)
- **ArticleRepository**: Gerencia a lista em memória de artigos
- **Responsabilidade**: CRUD operations (atualmente: findAll, saveAll)
- **Implementação**: CopyOnWriteArrayList (thread-safe)

### Data Storage
- **In-Memory Store**: CopyOnWriteArrayList mantida no ArticleRepository
- **Responsabilidade**: Armazenar dados de artigos inicializados via DataInitializer

---

## 4. Fluxos de Erro

### 401 Unauthorized
```
Client → ApiKeyInterceptor
         ↓
         Valida apikey
         ↓
         [INVÁLIDA] → Response 401 + UnauthorizedException
```

### 429 Too Many Requests
```
Client → ApiKeyInterceptor → RateLimitService
                            ↓
                            Verifica quota
                            ↓
                            [EXCEDIDA] → Response 429 + RateLimitExceededException
```

### 400 Bad Request
```
Validação no Controller ou Service
↓
Retorna GlobalExceptionHandler
↓
Response 400 + ErrorResponse
```

---

## 5. Atributos de Requisição por Endpoint

### /api/v4/top-headlines
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| apikey | string | ✅ Sim | Chave API para autenticação |
| category | string | ❌ Não | Categoria (breaking-news, world, nation, business, etc) |
| lang | string | ❌ Não | Código de idioma (ex: en, pt) |
| country | string | ❌ Não | Código de país (ex: us, br) |
| q | string | ❌ Não | Palavras-chave para filtro |
| max | int | ❌ Não | Máximo de resultados (default: 10, max: 100) |
| page | int | ❌ Não | Número da página (default: 1) |

### /api/v4/search
| Parâmetro | Tipo | Obrigatório | Descrição |
|-----------|------|-------------|-----------|
| apikey | string | ✅ Sim | Chave API para autenticação |
| q | string | ✅ Sim | Palavras-chave de busca (obrigatório) |
| lang | string | ❌ Não | Código de idioma |
| country | string | ❌ Não | Código de país |
| sortby | string | ❌ Não | Ordenação (publishedAt, relevance) |
| from | string | ❌ Não | Data inicial (ISO 8601) |
| to | string | ❌ Não | Data final (ISO 8601) |
| max | int | ❌ Não | Máximo de resultados (default: 10, max: 100) |
| page | int | ❌ Não | Número da página (default: 1) |
