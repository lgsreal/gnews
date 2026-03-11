package com.gnews.fake.domain;

import java.time.LocalDateTime;

public record Article(
        String id,
        String title,
        String descricao,
        String content,
        String url,
        String img,
        LocalDateTime publishedAt,
        String lang,
        String category, // Internal use for filtering
        Source src) {
}
