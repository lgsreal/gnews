package com.gnews.fake.controller;

import com.gnews.fake.dto.ArticlesResponse;
import com.gnews.fake.service.ArticleService;
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

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
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
        // API Key validation happens in Interceptor/Filter (to be implemented)
        return articleService.getTopHeadlines(category, lang, country, q, page, max);
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
