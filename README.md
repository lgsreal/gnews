# GNews Mock API - Exemplo de Vibecoding

Este projeto é um clone da API GNews, implementado inteiramente por agentes de IA utilizando **Antigravity**. O objetivo principal é servir como um exemplo educacional sobre o uso de ferramentas de IA avançadas para desenvolvimento de software ("vibecoding").

## 🤖 Sobre a Implementação

Todo o código e estrutura deste projeto foram gerados automaticamente:

1.  **Contexto Técnico**: O diretório `.ai/` (que contém regras de arquitetura e padrões) foi criado a partir do prompt disponível em [extras/prompt-contexto.md](extras/prompt-contexto.md).
2.  **Implementação**: A aplicação Spring Boot completa foi gerada seguindo o prompt disponível em [extras/prompt-implementacao.md](extras/prompt-implementacao.md).

## 🧪 Como Testar

O projeto inclui um cliente HTML simples para testar a API localmente.

1.  Construa o projeto (baixa dependências):
    ```bash
    mvn clean install -DskipTests
    ```
2.  Inicie a aplicação Java (Spring Boot):
    ```bash
    mvn spring-boot:run
    ```
2.  **Cliente HTML**: Abra o arquivo [extras/news-site.html](extras/news-site.html) em seu navegador. A "API Key" de teste já está configurada (`test-api-key`).
3.  **Swagger UI**: Alternativamente, acesse `http://localhost:8080/swagger-ui.html` para explorar e testar a API.

## � Endpoints Principais

A API reproduz as seguintes rotas da GNews (V4):

*   `GET /api/v4/top-headlines`: Retorna notícias principais (filtro por categoria, país, idioma).
*   `GET /api/v4/search`: Busca notícias por palavras-chave.

> **Nota:** Todas as chamadas exigem o parâmetro `apikey=test-api-key`.

## �📁 Estrutura de Arquivos Extras

Os arquivos utilizados para gerar e testar este projeto foram movidos para a pasta `extras/` para organização:

*   `extras/prompt-contexto.md`: Prompt usado para gerar o contexto técnico.
*   `extras/prompt-implementacao.md`: Prompt usado para gerar o código.
*   `extras/news-site.html`: Cliente web interativo para demonstração.

## 🚀 Tecnologias

*   Java 25
*   Spring Boot
*   Maven
*   Antigravity (AI Agent)

