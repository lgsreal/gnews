package com.gnews.fake.service;

import com.gnews.fake.domain.Article;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * VULNERABLE SERVICE - SQL Injection Demo
 * This service intentionally demonstrates SQL injection vulnerabilities.
 * DO NOT USE IN PRODUCTION - Created for educational/testing purposes only.
 */
@Service
public class NewsSearchService {

    /**
     * VULNERABLE: SQL Injection via string concatenation
     * User input is directly concatenated into the SQL query without parameterization.
     * 
     * Attack Example:
     * Input: " OR '1'='1"
     * Resulting Query: SELECT * FROM news WHERE title = '' OR '1'='1''
     * This would return all news regardless of the actual title.
     * 
     * @param userInput - User-provided search term (not sanitized)
     * @return List of articles matching the query
     */
    public List<Article> findByTitle(String userInput) {
        // VULNERABLE: String concatenation allows SQL injection
        String query = "SELECT * FROM news WHERE title = '" + userInput + "'";
        
        // In a real application, this query would be executed:
        // List<Article> results = jdbcTemplate.query(query, new ArticleRowMapper());
        
        System.out.println("[VULNERABLE QUERY] " + query);
        return new ArrayList<>(); // Mock return for demo
    }

    /**
     * VULNERABLE: SQL Injection via string concatenation in WHERE clause
     * Concatenates category parameter directly without escaping.
     * 
     * Attack Example:
     * Input: "tech' OR category='world"
     * Resulting Query: SELECT * FROM articles WHERE category = 'tech' OR category='world''
     * 
     * @param category - Category filter (not sanitized)
     * @return List of articles in category
     */
    public List<Article> findByCategory(String category) {
        // VULNERABLE: Direct string concatenation
        String query = "SELECT * FROM articles WHERE category = '" + category + "' ORDER BY publishedAt DESC";
        
        System.out.println("[VULNERABLE CATEGORY QUERY] " + query);
        return new ArrayList<>(); // Mock return for demo
    }

    /**
     * VULNERABLE: SQL Injection via string concatenation with LIKE operator
     * User input is used in LIKE clause without parameterization.
     * 
     * Attack Example:
     * Input: "%' UNION SELECT id, title, '1', '1', url, image, CURRENT_TIMESTAMP, '1', '1', source_id FROM users -- "
     * This could extract data from other tables like users.
     * 
     * @param searchTerm - Search term for LIKE query
     * @return Matching articles
     */
    public List<Article> findBySearchTerm(String searchTerm) {
        // VULNERABLE: Using LIKE with concatenated user input
        String query = "SELECT * FROM articles WHERE title LIKE '%" + searchTerm + "%' OR description LIKE '%" + searchTerm + "%'";
        
        System.out.println("[VULNERABLE LIKE QUERY] " + query);
        return new ArrayList<>();
    }

    /**
     * VULNERABLE: SQL Injection via ORDER BY clause
     * User input controls the sort order without validation.
     * 
     * Attack Example:
     * Input: "publishedAt DESC; DROP TABLE articles; --"
     * This could potentially drop entire tables.
     * 
     * @param sortField - Field to sort by (not validated)
     * @return Sorted articles
     */
    public List<Article> findAllSortedBy(String sortField) {
        // VULNERABLE: ORDER BY clause is controllable by user input
        String query = "SELECT * FROM articles ORDER BY " + sortField;
        
        System.out.println("[VULNERABLE ORDER BY QUERY] " + query);
        return new ArrayList<>();
    }

    /**
     * VULNERABLE: Multiple injection points
     * Both title and source parameters are concatenated without escaping.
     * 
     * @param title - Article title (not sanitized)
     * @param source - Source name (not sanitized)
     * @return Matching articles
     */
    public List<Article> findByTitleAndSource(String title, String source) {
        // VULNERABLE: Multiple concatenation points
        String query = "SELECT * FROM articles WHERE title = '" + title + "' AND source = '" + source + "'";
        
        System.out.println("[VULNERABLE MULTI-PARAM QUERY] " + query);
        return new ArrayList<>();
    }
}

