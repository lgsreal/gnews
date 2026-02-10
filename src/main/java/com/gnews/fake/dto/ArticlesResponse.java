package com.gnews.fake.dto;

import java.util.List;

public record ArticlesResponse(
                long total_articles,
                List<ArticleDto> articles) {
}
