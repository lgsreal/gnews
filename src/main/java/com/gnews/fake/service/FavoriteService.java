package com.gnews.fake.service;

import com.gnews.fake.dto.FavoriteRequest;
import com.gnews.fake.dto.FavoriteResponse;
import org.springframework.stereotype.Service;

@Service
public class FavoriteService {
    private final com.gnews.fake.repository.FavoriteRepository favoriteRepository;

    public FavoriteService(com.gnews.fake.repository.FavoriteRepository favoriteRepository) {
        this.favoriteRepository = favoriteRepository;
    }

    public FavoriteResponse addFavorite(FavoriteRequest request) {
        var favorite = new com.gnews.fake.domain.Favorite(request.articleId(), request.userId());
        var saved = favoriteRepository.save(favorite);
        return new FavoriteResponse(saved.getId(), saved.getArticleId(), saved.getUserId(), saved.getFavoritedAt());
    }
}
