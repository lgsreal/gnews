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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import com.gnews.fake.dto.ArticleDto;

@RestController
@RequestMapping("/api/v4")
@Validated
@Tag(name = "GNews API v4", description = "Mock implementation of GNews API")
public class ArticleController {

    private final ArticleService articleService;

    // VIOLATION: Optional in field (Bad Practice & Standards Violation)
    private Optional<String> lastSearchQuery = Optional.empty();

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

    // VIOLATION: High Cyclomatic Complexity, Manual Loops (No Streams), Logic in
    // Controller
    @GetMapping("/analyze")
    @Operation(summary = "Analyze articles complexity", description = "Analyzes articles with high complexity logic")
    public List<String> analyzeArticles(@RequestParam(required = false) List<String> keywords) {
        // Fetch all (mock)
        List<ArticleDto> allArticles = articleService.getTopHeadlines(null, null, null, null, 1, 100).articles();
        List<String> results = new ArrayList<>();

        if (keywords == null) {
            keywords = new ArrayList<>();
            keywords.add("news");
        }

        // Manual Loops instead of Streams (Standards Violation)
        for (int i = 0; i < allArticles.size(); i++) {
            ArticleDto a1 = allArticles.get(i);
            for (int j = 0; j < keywords.size(); j++) {
                String k = keywords.get(j);
                if (a1.title() != null) {
                    if (a1.title().toLowerCase().contains(k.toLowerCase())) {
                        boolean alreadyExists = false;
                        // Manual check for existence
                        for (int m = 0; m < results.size(); m++) {
                            if (results.get(m).equals(a1.title())) {
                                alreadyExists = true;
                                break;
                            }
                        }

                        if (!alreadyExists) {
                            if (a1.description() != null) {
                                if (a1.description().length() > 5) { // Arbitrary logic
                                    // Deeply nested if
                                    if (a1.url() != null && !a1.url().isEmpty()) {
                                        results.add(a1.title());
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return results;
    }

    // VIOLATION: DTO as Class instead of Record (Standards Violation)
    public static class BadDto {
        public String query;
    }

    @GetMapping("/bad-dto")
    public BadDto getBadDto() {
        BadDto d = new BadDto();
        d.query = "validation";
        return d;
    }
}
