# Diagramas de Arquitetura - GNews Fake API

## Consultas e Relacionamentos (ERD)

Este diagrama representa o modelo de dados persistido via JPA (H2 Database).

```mermaid
erDiagram
    ARTICLE {
        string id PK
        string title
        string description
        string content
        string url
        string image
        datetime published_at
        string lang
        string category
        string source_id FK
    }
    SOURCE {
        string id PK
        string name
        string url
        string country
    }

    ARTICLE }|--|| SOURCE : "belongs to"
```

## Fluxo de Execução (Diagrama de Sequência)

Este diagrama detalha o fluxo de uma requisição para listar as principais notícias (`top-headlines`), incluindo a validação de segurança simulada.

```mermaid
sequenceDiagram
    participant Client
    participant API_Gateway as API Key Filter
    participant Controller as ArticleController
    participant Service as ArticleService
    participant Repo as ArticleRepository
    participant DB as H2 Database

    Client->>API_Gateway: GET /api/v4/top-headlines?apikey=xyz
    
    rect rgb(240, 240, 240)
        Note over API_Gateway: Validação de Segurança
        alt API Key Inválida
            API_Gateway-->>Client: 401 Unauthorized
        else API Key Válida
            API_Gateway->>Controller: getTopHeadlines(category, lang, ...)
        end
    end

    activate Controller
    Controller->>Service: getTopHeadlines(filters...)
    activate Service
    
    Service->>Repo: findAll()
    activate Repo
    Repo->>DB: SELECT * FROM articles
    DB-->>Repo: ResultSet (Entities)
    Repo-->>Service: List<Article>
    deactivate Repo

    Service->>Service: Filter (Stream API)
    Service->>Service: Map to DTOs
    
    Service-->>Controller: ArticlesResponse (DTO)
    deactivate Service

    Controller-->>Client: 200 OK (JSON)
    deactivate Controller
```
