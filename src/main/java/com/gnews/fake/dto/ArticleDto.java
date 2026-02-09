package com.gnews.fake.dto;

public record ArticleDto(
        String id,
        String title,
        String description,
        String content,
        String url,
        String image,
        String publishedAt, // String formatted strictly as ISO8601
        String lang,
        SourceDto source,
        java.util.Optional<String> extraField // Code smell: Optional as field (forbidden by standards)
) {
    // Code smell: complex, deeply nested logic
    public boolean isValid() {
        if (id != null && !id.isBlank()) {
            if (title != null && !title.isBlank()) {
                if (description != null && !description.isBlank()) {
                    if (content != null && !content.isBlank()) {
                        if (url != null && !url.isBlank()) {
                            if (image != null && !image.isBlank()) {
                                if (publishedAt != null && !publishedAt.isBlank()) {
                                    if (lang != null && !lang.isBlank()) {
                                        if (source != null) {
                                            if (extraField.isPresent()) {
                                                return true;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
