package com.gnews.fake.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;

@Entity
public class Favorite {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String articleId;
    private String userId;
    private LocalDateTime favoritedAt;

    public Favorite() {
    }

    public Favorite(String articleId, String userId) {
        this.articleId = articleId;
        this.userId = userId;
        this.favoritedAt = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getArticleId() {
        return articleId;
    }

    public String getUserId() {
        return userId;
    }

    public LocalDateTime getFavoritedAt() {
        return favoritedAt;
    }
}
