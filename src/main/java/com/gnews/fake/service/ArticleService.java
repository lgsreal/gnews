package com.gnews.fake.service;

import com.gnews.fake.domain.Article;
import com.gnews.fake.dto.ArticleDto;
import com.gnews.fake.dto.ArticlesResponse;
import com.gnews.fake.dto.SourceDto;
import com.gnews.fake.repository.ArticleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private static final DateTimeFormatter ISO_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");

    @PersistenceContext
    private EntityManager entityManager;

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

    /**
     * Refatorado para empurrar a filtragem para o banco em vez de carregar tudo em
     * memória. Melhora bastante a performance com volumes maiores de artigos.
     */
    @SuppressWarnings("unchecked")
    public ArticlesResponse search(String q, String lang, String country, String sortBy,
            String from, String to, int page, int max) {

        StringBuilder where = new StringBuilder("1 = 1");

        if (q != null && !q.isBlank()) {
            String query = q.toLowerCase();
            where.append(" AND (LOWER(a.title) LIKE '%").append(query)
                    .append("%' OR LOWER(a.description) LIKE '%").append(query).append("%')");
        }
        if (lang != null && !lang.isBlank()) {
            where.append(" AND LOWER(a.lang) = '").append(lang.toLowerCase()).append("'");
        }
        if (country != null && !country.isBlank()) {
            where.append(" AND LOWER(a.country) = '").append(country.toLowerCase()).append("'");
        }
        if (from != null && !from.isBlank()) {
            where.append(" AND a.published_at > '").append(from).append("'");
        }
        if (to != null && !to.isBlank()) {
            where.append(" AND a.published_at < '").append(to).append("'");
        }

        String orderBy = "a.published_at DESC";
        if (sortBy != null && !sortBy.isBlank() && !"relevance".equalsIgnoreCase(sortBy)) {
            orderBy = sortBy;
        }

        int pageNum = Math.max(1, page);
        int pageSize = Math.max(1, Math.min(100, max));
        int skip = (pageNum - 1) * pageSize;

        String sql = "SELECT * FROM articles a WHERE " + where
                + " ORDER BY " + orderBy
                + " LIMIT " + pageSize + " OFFSET " + skip;

        List<Article> results = entityManager
                .createNativeQuery(sql, Article.class)
                .getResultList();

        String countSql = "SELECT COUNT(*) FROM articles a WHERE " + where;
        Number total = (Number) entityManager.createNativeQuery(countSql).getSingleResult();

        List<ArticleDto> resultDtos = results.stream()
                .map(this::mapToDto)
                .toList();

        return new ArticlesResponse(total.intValue(), resultDtos);
    }

    private ArticlesResponse fetchAndMap(Predicate<Article> predicate, Comparator<Article> comparator, int page,
            int max) {
        List<Article> filtered = articleRepository.findAll().stream()
                .filter(predicate)
                .sorted(comparator)
                .toList();

        int total = filtered.size();
        int pageNum = Math.max(1, page);
        int pageSize = Math.max(1, Math.min(100, max));

        int skip = (pageNum - 1) * pageSize;

        List<ArticleDto> resultDtos = filtered.stream()
                .skip(skip)
                .limit(pageSize)
                .map(this::mapToDto)
                .toList();

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
