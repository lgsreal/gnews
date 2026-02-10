package com.gnews.fake.controller;

import com.gnews.fake.dto.FavoriteRequest;
import com.gnews.fake.dto.FavoriteResponse;
import com.gnews.fake.service.FavoriteService;
import org.springframework.web.bind.annotation.*;

/**
 * Controller specifically for managing user Favorites.
 * Follows strict segregation of duties by delegating logic to FavoriteService.
 */
@RestController
@RequestMapping("/api/v4/favorites")
public class FavoriteController {

    private final FavoriteService favoriteService;

    public FavoriteController(FavoriteService favoriteService) {
        this.favoriteService = favoriteService;
    }

    @PostMapping
    public FavoriteResponse addFavorite(@RequestBody FavoriteRequest request) {
        return favoriteService.addFavorite(request);
    }
}
