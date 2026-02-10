# Diagrama ERD (Baseado no Domínio)

Como o projeto atualmente utiliza `records` Java para o modelo de domínio e não possui entidades JPA (`@Entity`), este diagrama representa a estrutura lógica baseada nas classes de domínio existentes (`Article` e `Source`).

```mermaid
erDiagram
    ARTICLE {
        String id PK
        String title
        String description
        String content
        String url
        String image
        LocalDateTime publishedAt
        String lang
        String category
    }
    SOURCE {
        String id PK
        String name
        String url
        String country
    }
    ARTICLE }o--|| SOURCE : "source"
```

# Diagrama de Sequência (Fluxo de Endpoint)

Este diagrama demonstra o processo de uma requisição para o endpoint `/api/v4/top-headlines`, incluindo validação da chave da API, verificação de rate limit e consulta aos dados.

```mermaid
sequenceDiagram
    autonumber
    
    actor Client
    participant Interceptor as ApiKeyInterceptor (Security)
    participant RateLimit as RateLimitService
    participant Controller as ArticleController
    participant Service as ArticleService
    participant Repository as ArticleRepository

    Client->>Interceptor: GET /api/v4/top-headlines?apikey=...
    
    activate Interceptor
    Note right of Interceptor: Validação de Segurança
    
    Interceptor->>Interceptor: Verificar existência da API Key
    
    alt API Key Inválida ou Ausente
        Interceptor-->>Client: 401 Unauthorized
    else API Key Válida
        Interceptor->>RateLimit: allowRequest(apikey)
        activate RateLimit
        
        alt Limite Excedido
            RateLimit-->>Interceptor: false
            Interceptor-->>Client: 429 Too Many Requests
        else Limite OK
            RateLimit-->>Interceptor: true
            deactivate RateLimit
            
            Interceptor->>Controller: Encaminhar Requisição (preHandle=true)
            deactivate Interceptor
            
            activate Controller
            Controller->>Service: getTopHeadlines(category, lang, ...)
            
            activate Service
            Service->>Repository: findAll()
            activate Repository
            Repository-->>Service: List<Article> (Todos os artigos)
            deactivate Repository
            
            Note right of Service: Lógica de Negócio
            Service->>Service: Filtrar (Predicate)
            Service->>Service: Ordenar (Comparator)
            Service->>Service: Paginar (Skip/Limit)
            Service->>Service: Mapear para DTOs
            
            Service-->>Controller: ArticlesResponse
            deactivate Service
            
            Controller-->>Client: 200 OK (JSON)
            deactivate Controller
        end
    end
```
