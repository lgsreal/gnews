package com.gnews.fake.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import java.util.List;
import java.util.Map;

@RestController
public class SearchController {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * Endpoint vulnerável a SQL Injection conforme solicitado no laboratório.
     * A entrada do usuário é concatenada diretamente na query.
     */
    @GetMapping("/api/v4/search/vulnerable")
    public List<Map<String, Object>> searchArticles(@RequestParam String query) {
        // VULNERABILIDADE PROPOSITAL: Concatenando entrada do usuário diretamente na consulta SQL
        String sql = "SELECT * FROM articles WHERE title = '" + query + "'";
        
        System.out.println("Executando query vulnerável: " + sql);
        
        return jdbcTemplate.queryForList(sql);
    }
}
// Triggering AI review
