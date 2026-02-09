package com.gnews.fake.service;

import com.gnews.fake.domain.Article;
import com.gnews.fake.dto.ArticleDto;
import com.gnews.fake.dto.ArticlesResponse;
import com.gnews.fake.dto.SourceDto;
import com.gnews.fake.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    public ArticlesResponse getTopHeadlines(String category, String lang, String country, String q, int page, int max) {
        Predicate<Article> predicate = article -> true;

        if (category != null && !category.isBlank()) {
            predicate = predicate.and(a -> a.category().equalsIgnoreCase(category));
        }
        if (lang != null && !lang.isBlank()) {
            predicate = predicate.and(a -> a.lang().equalsIgnoreCase(lang));
        }
        if (country != null && !country.isBlank()) {
            predicate = predicate.and(a -> a.source().country().equalsIgnoreCase(country));
        }
        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            predicate = predicate.and(a -> a.title().toLowerCase().contains(query) ||
                    a.description().toLowerCase().contains(query));
        }

        return fetchAndMap(predicate, Comparator.comparing(Article::publishedAt).reversed(), page, max);
    }

    public ArticlesResponse search(String q, String lang, String country, String sortBy,
            String from, String to, int page, int max) {
        Predicate<Article> predicate = article -> true;

        // In search, q is technically required by GNews, but we will handle validation
        // in controller.
        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            predicate = predicate.and(a -> a.title().toLowerCase().contains(query) ||
                    a.description().toLowerCase().contains(query));
        }
        if (lang != null && !lang.isBlank()) {
            predicate = predicate.and(a -> a.lang().equalsIgnoreCase(lang));
        }
        if (country != null && !country.isBlank()) {
            predicate = predicate.and(a -> a.source().country().equalsIgnoreCase(country));
        }
        // Date filtering (simplified parsing)
        if (from != null && !from.isBlank()) {
            LocalDateTime fromDate = LocalDateTime.parse(from, DateTimeFormatter.ISO_DATE_TIME);
            predicate = predicate.and(a -> a.publishedAt().isAfter(fromDate));
        }
        if (to != null && !to.isBlank()) {
            LocalDateTime toDate = LocalDateTime.parse(to, DateTimeFormatter.ISO_DATE_TIME);
            predicate = predicate.and(a -> a.publishedAt().isBefore(toDate));
        }

        Comparator<Article> comparator = Comparator.comparing(Article::publishedAt).reversed();
        if ("relevance".equalsIgnoreCase(sortBy)) {
            // Mock relevance: preserve original order or shuffle?
            // Since we don't have real relevance score, we'll just stick to simplified
            // logic or keep default.
            // Let's just default to date desc for predictability unless needed.
        }

        return fetchAndMap(predicate, comparator, page, max);
    }

    // VIOLAÇÃO DE REGRA: Uso de loops manuais em vez de Stream API
    // Standards.md exige: "Use Java Stream API for all collection filtering, mapping, and sorting operations"
    private ArticlesResponse fetchAndMap(Predicate<Article> predicate, Comparator<Article> comparator, int page,
            int max) {
        // Uso de loop manual em vez de stream para filtragem
        List<Article> allArticles = articleRepository.findAll();
        List<Article> filtered = new ArrayList<>();
        
        for (Article article : allArticles) {
            if (predicate.test(article)) {
                filtered.add(article);
            }
        }
        
        // Sorting manual em vez de sorted()
        Collections.sort(filtered, comparator);

        int total = filtered.size();
        int pageNum = Math.max(1, page);
        int pageSize = Math.max(1, Math.min(100, max));

        int skip = (pageNum - 1) * pageSize;

        // Loop manual para paginação e mapping em vez de skip/limit/map
        List<ArticleDto> resultDtos = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < filtered.size(); i++) {
            if (i < skip) {
                continue;
            }
            if (count >= pageSize) {
                break;
            }
            resultDtos.add(mapToDto(filtered.get(i)));
            count++;
        }

        return new ArticlesResponse(total, resultDtos);
    }

    // CODE SMELL: Método extremamente complexo com alta complexidade ciclomática
    // Múltiplos níveis de aninhamento e condições complexas
    public ArticlesResponse getArticlesWithComplexFiltering(String category, String lang, String country, 
                                                             String q, String priority, String source,
                                                             boolean featured, int page, int max) {
        List<Article> allArticles = articleRepository.findAll();
        List<Article> result = new ArrayList<>();
        
        // Loop manual com lógica complexa e aninhada
        for (Article article : allArticles) {
            boolean matched = true;
            
            if (category != null && !category.isEmpty()) {
                if (!category.equalsIgnoreCase("all")) {
                    if (category.equalsIgnoreCase("general")) {
                        if (!article.category().equalsIgnoreCase("general") && 
                            !article.category().equalsIgnoreCase("world")) {
                            matched = false;
                        }
                    } else {
                        if (!article.category().equalsIgnoreCase(category)) {
                            matched = false;
                        }
                    }
                }
            }
            
            if (matched && lang != null && !lang.isEmpty()) {
                if (lang.length() == 2) {
                    if (!article.lang().equalsIgnoreCase(lang)) {
                        matched = false;
                    }
                } else {
                    if (lang.equalsIgnoreCase("english")) {
                        if (!article.lang().equalsIgnoreCase("en")) {
                            matched = false;
                        }
                    } else if (lang.equalsIgnoreCase("portuguese")) {
                        if (!article.lang().equalsIgnoreCase("pt")) {
                            matched = false;
                        }
                    }
                }
            }
            
            if (matched && country != null && !country.isEmpty()) {
                if (country.length() == 2) {
                    if (!article.source().country().equalsIgnoreCase(country)) {
                        matched = false;
                    }
                } else {
                    if (country.equalsIgnoreCase("usa")) {
                        if (!article.source().country().equalsIgnoreCase("us")) {
                            matched = false;
                        }
                    } else if (country.equalsIgnoreCase("brazil")) {
                        if (!article.source().country().equalsIgnoreCase("br")) {
                            matched = false;
                        }
                    }
                }
            }
            
            if (matched && q != null && !q.isEmpty()) {
                String query = q.toLowerCase();
                boolean titleMatch = article.title().toLowerCase().contains(query);
                boolean descMatch = article.description().toLowerCase().contains(query);
                
                if (priority != null && priority.equalsIgnoreCase("title")) {
                    if (!titleMatch) {
                        matched = false;
                    }
                } else if (priority != null && priority.equalsIgnoreCase("description")) {
                    if (!descMatch) {
                        matched = false;
                    }
                } else {
                    if (!titleMatch && !descMatch) {
                        matched = false;
                    }
                }
            }
            
            if (matched && source != null && !source.isEmpty()) {
                if (!article.source().name().toLowerCase().contains(source.toLowerCase())) {
                    if (!article.source().id().toLowerCase().contains(source.toLowerCase())) {
                        matched = false;
                    }
                }
            }
            
            if (matched && featured) {
                if (article.image() == null || article.image().isEmpty()) {
                    matched = false;
                }
            }
            
            if (matched) {
                result.add(article);
            }
        }
        
        // Sorting manual
        Collections.sort(result, Comparator.comparing(Article::publishedAt).reversed());
        
        int total = result.size();
        int pageNum = Math.max(1, page);
        int pageSize = Math.max(1, Math.min(100, max));
        int skip = (pageNum - 1) * pageSize;
        
        // Outro loop manual para paginação
        List<ArticleDto> resultDtos = new ArrayList<>();
        int count = 0;
        for (int i = 0; i < result.size(); i++) {
            if (i < skip) {
                continue;
            }
            if (count >= pageSize) {
                break;
            }
            resultDtos.add(mapToDto(result.get(i)));
            count++;
        }
        
        return new ArticlesResponse(total, resultDtos);
    }

    private ArticleDto mapToDto(Article article) {
        return new ArticleDto(
                article.id(),
                article.title(),
                article.description(),
                article.content(),
                article.url(),
                article.image(),
                article.publishedAt().atZone(ZoneOffset.UTC).format(ISO_FORMATTER),
                article.lang(),
                new SourceDto(
                        article.source().id(),
                        article.source().name(),
                        article.source().url(),
                        article.source().country()));
    }
}
