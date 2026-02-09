package com.gnews.fake.dto;

import java.util.List;

public record ArticlesResponse(
        long totalArticles,
        ist<ArticleDto> articles) {
}
