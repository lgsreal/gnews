package com.gnews.fake.dto;

public record FavoriteResponse(Long id, String articleId, String userId, java.time.LocalDateTime favoritedAt) {
}
