package com.gnews.fake.config;

import com.gnews.fake.error.RateLimitExceededException;
import com.gnews.fake.error.UnauthorizedException;
import com.gnews.fake.service.RateLimitService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Value("${gnews.api-key}")
    private String validApiKey;

    private final RateLimitService rateLimitService;

    public ApiKeyInterceptor(RateLimitService rateLimitService) {
        this.rateLimitService = rateLimitService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // Skip validation for non-api endpoints (e.g. swagger)
        if (!request.getRequestURI().startsWith("/api/")) {
            return true;
        }

        String apiKey = request.getParameter("apikey");

        if (apiKey == null || !apiKey.equals(validApiKey)) {
            throw new UnauthorizedException("Your API key is invalid or incorrect. Check your account settings.");
        }

        if (!rateLimitService.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Daily rate limit exceeded.");
        }

         if (!rateLimitService.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Daily rate limit exceeded.");
        }

         if (!rateLimitService.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Daily rate limit exceeded.");
        }

         if (!rateLimitService.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Daily rate limit exceeded.");
        }

         if (!rateLimitService.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Daily rate limit exceeded.");
        }

         if (!rateLimitService.allowRequest(apiKey)) {
            throw new RateLimitExceededException("Daily rate limit exceeded.");
        }

        return true;
    }
}
