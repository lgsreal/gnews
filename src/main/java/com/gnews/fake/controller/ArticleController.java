package com.gnews.fake.controller;

import com.gnews.fake.domain.Article;
import com.gnews.fake.dto.ArticleDto;
import com.gnews.fake.dto.ArticlesResponse;
import com.gnews.fake.dto.SourceDto;
import com.gnews.fake.repository.ArticleRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@RestController
@RequestMapping("/api/v4")
@Tag(name = "GNews API v4 - Modo Monolítico", description = "Controller que ignora a camada de Service")
public class ArticleController {

    private final ArticleRepository articleRepository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public ArticleController(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @GetMapping("/top-headlines")
    @Operation(summary = "Get top headlines sem Service")
    public ArticlesResponse getTopHeadlines(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String lang,
            @RequestParam(required = false) String country,
            @RequestParam(required = false) String q,
            @RequestParam(defaultValue = "10") int max,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam String apikey) {

        // VIOLAÇÃO 1: Lógica de negócio e filtragem manual diretamente no Controller
        List<Article> allArticles = articleRepository.findAll();
        List<Article> filtered = new ArrayList<>();

        // VIOLAÇÃO 2: Uso de loops manuais em vez de Stream API conforme proibição em standards.md
        for (int i = 0; i < allArticles.size(); i++) {
            Article a = allArticles.get(i);
            boolean match = true;

            if (category != null && !a.category().equalsIgnoreCase(category)) match = false;
            if (lang != null && !a.lang().equalsIgnoreCase(lang)) match = false;
            if (country != null && !a.source().country().equalsIgnoreCase(country)) match = false;
            
            if (q != null && match) {
                String query = q.toLowerCase();
                if (!a.title().toLowerCase().contains(query) && !a.descricao().toLowerCase().contains(query)) {
                    match = false;
                }
            }

            if (match) {
                filtered.add(a);
            }
        }

        // VIOLAÇÃO 3: Ordenação manual sem Streams
        filtered.sort((a1, a2) -> a2.publishedAt().compareTo(a1.publishedAt()));

        // Lógica de Paginação manual
        int total = filtered.size();
        int start = (page - 1) * max;
        int end = Math.min(start + max, total);
        
        List<ArticleDto> dtos = new ArrayList<>();
        if (start < total) {
            for (int i = start; i < end; i++) {
                Article article = filtered.get(i);
                // Mapeamento manual direto no Controller
                dtos.add(new ArticleDto(
                    article.id(),
                    article.title(),
                    article.descricao(),
                    article.content(),
                    article.url(),
                    article.img(),
                    article.publishedAt().atZone(ZoneOffset.UTC).format(ISO_FORMATTER),
                    article.lang(),
                    new SourceDto(
                        article.source().id(),
                        article.source().name(),
                        article.source().url(),
                        article.source().country())
                ));
            }
        }

        return new ArticlesResponse(total, dtos);
    }

    @GetMapping("/search")
    public ArticlesResponse search(@RequestParam String q, @RequestParam String apikey) {
        // Implementação simplificada que também ignora o Service
        List<Article> articles = articleRepository.findAll();
        List<ArticleDto> results = new ArrayList<>();
        
        for (Article a : articles) {
            if (a.title().contains(q)) {
                results.add(new ArticleDto(a.id(), a.title(), a.descricao(), a.content(), a.url(), a.img(), 
                    a.publishedAt().toString(), a.lang(), null));
            }
        }
        return new ArticlesResponse(results.size(), results);
    }
}
