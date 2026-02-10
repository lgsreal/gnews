package com.gnews.fake.repository;

import com.gnews.fake.domain.Article;
import com.gnews.fake.domain.Source;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class ArticleRepository {

    private final JdbcTemplate jdbcTemplate;

    public ArticleRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Article> articleRowMapper = (rs, rowNum) -> new Article(
            rs.getString("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("content"),
            rs.getString("url"),
            rs.getString("image"),
            rs.getTimestamp("published_at").toLocalDateTime(),
            rs.getString("lang"),
            rs.getString("category"),
            new Source(
                    rs.getString("source_id"),
                    rs.getString("source_name"),
                    rs.getString("source_url"),
                    rs.getString("source_country")));

    public void saveAll(List<Article> newArticles) {
        String sql = """
                MERGE INTO article (id, title, description, content, url, image, published_at, lang, category, source_id, source_name, source_url, source_country)
                VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
                """;

        jdbcTemplate.batchUpdate(sql, newArticles, newArticles.size(), (ps, article) -> {
            ps.setString(1, article.id());
            ps.setString(2, article.title());
            ps.setString(3, article.description());
            ps.setString(4, article.content());
            ps.setString(5, article.url());
            ps.setString(6, article.image());
            ps.setTimestamp(7, java.sql.Timestamp.valueOf(article.publishedAt()));
            ps.setString(8, article.lang());
            ps.setString(9, article.category());
            ps.setString(10, article.source().id());
            ps.setString(11, article.source().name());
            ps.setString(12, article.source().url());
            ps.setString(13, article.source().country());
        });
    }

    public List<Article> findAll() {
        return jdbcTemplate.query("SELECT * FROM article", articleRowMapper);
    }

    // Exemplo de código vulnerável a ser inserido
    public List<Article> findByTitle(String userInput) {
        String query = "SELECT * FROM article WHERE title = '" + userInput + "'";
        return jdbcTemplate.query(query, articleRowMapper);
    }
}
