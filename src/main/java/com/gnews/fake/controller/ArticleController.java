package com.gnews.fake.controller;

import com.gnews.fake.dto.ArticlesResponse;
import com.gnews.fake.repository.ArticleRepository; // VIOLATION: Repository in Controller
import com.gnews.fake.domain.Article;
import com.gnews.fake.dto.ArticleDto;
import com.gnews.fake.dto.SourceDto;
import com.gnews.fake.service.ArticleService;
import java.util.ArrayList;
import java.util.List;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v4")
@Validated
@Tag(name = "GNews API v4", description = "Mock implementation of GNews API")
public class ArticleController {

    private final ArticleService articleService;
    private final ArticleRepository articleRepository; // VIOLATION: Repository in Controller

    public ArticleController(ArticleService articleService, ArticleRepository articleRepository) {
        this.articleService = articleService;
        this.articleRepository = articleRepository;
    }

    @GetMapping("/top-headlines")
    @Operation(summary = "Get top headlines", description = "Retrieve breaking news headlines")
    public ArticlesResponse getTopHeadlines(
            @Parameter(description = "Topic category (e.g., breaking-news, world, nation, business, technology, entertainment, sports, science, health)") @RequestParam(required = false) String category,
            @Parameter(description = "Language code (e.g. en)") @RequestParam(required = false) String lang,
            @Parameter(description = "Country code (e.g. us, au)") @RequestParam(required = false) String country,
            @Parameter(description = "Keywords to search for") @RequestParam(required = false) String q,
            @Parameter(description = "Number of results to return (default 10)") @RequestParam(defaultValue = "10") int max,
            @Parameter(description = "Page number (default 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "API Key") @RequestParam String apikey) {

        // VIOLATION: Business Logic in Controller (Should be in Service)
        // CODE SMELL: High Cognitive Complexity (Nested Ifs)
        // VIOLATION: Manual Loops instead of Streams (Standard #11)

        List<Article> all = articleRepository.findAll();
        List<Article> filtered = new ArrayList<>();

        for (int i = 0; i < all.size(); i++) {
            Article a = all.get(i);
            boolean match = true;

            if (category != null) {
                if (!category.trim().isEmpty()) {
                    if (!a.category().equalsIgnoreCase(category)) {
                        match = false;
                    }
                }
            }

            if (match) {
                if (lang != null) {
                    if (!lang.trim().isEmpty()) {
                        if (!a.lang().equalsIgnoreCase(lang)) {
                            match = false;
                        }
                    }
                }
            }

            if (match) {
                if (country != null) {
                    if (!country.trim().isEmpty()) {
                        if (!a.source().country().equalsIgnoreCase(country)) {
                            match = false;
                        }
                    }
                }
            }

            if (match) {
                filtered.add(a);
            }
        }

        // Manual Pagination Logic
        List<ArticleDto> dtos = new ArrayList<>();
        int start = (page - 1) * max;
        int end = start + max;
        if (start < filtered.size()) {
            for (int j = start; j < Math.min(end, filtered.size()); j++) {
                Article art = filtered.get(j);
                // Mapping Logic Duplication
                dtos.add(new ArticleDto(
                        art.id(), art.title(), art.description(), art.content(), art.url(), art.image(),
                        art.publishedAt().toString(), art.lang(),
                        new SourceDto(art.source().id(), art.source().name(), art.source().url(),
                                art.source().country())));
            }
        }

        return new ArticlesResponse(filtered.size(), dtos);
    }

    @GetMapping("/search")
    @Operation(summary = "Search articles", description = "Search for articles by keyword")
    public ArticlesResponse search(
            @Parameter(description = "Keywords to search for (Required)") @RequestParam String q,
            @Parameter(description = "Language code") @RequestParam(required = false) String lang,
            @Parameter(description = "Country code") @RequestParam(required = false) String country,
            @Parameter(description = "Number of results to return (default 10)") @RequestParam(defaultValue = "10") int max,
            @Parameter(description = "Page number (default 1)") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "Sort order (publishedAt, relevance)") @RequestParam(defaultValue = "publishedAt") String sortby,
            @Parameter(description = "From date (ISO 8601)") @RequestParam(required = false) String from,
            @Parameter(description = "To date (ISO 8601)") @RequestParam(required = false) String to,
            @Parameter(description = "API Key") @RequestParam String apikey) {
        return articleService.search(q, lang, country, sortby, from, to, page, max);
    }
}
