package com.gnews.fake.integration;

import com.gnews.fake.dto.FavoriteRequest;
import com.gnews.fake.dto.FavoriteResponse;
import com.gnews.fake.repository.FavoriteRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
// import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// import org.testcontainers.containers.PostgreSQLContainer;
// import org.testcontainers.junit.jupiter.Container;
// import org.testcontainers.junit.jupiter.Testcontainers;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
// @Testcontainers // TODO: Enable when Docker is available
class FavoriteIntegrationTest {

    // @Container
    // @ServiceConnection
    // static PostgreSQLContainer<?> postgres = new
    // PostgreSQLContainer<>("postgres:16-alpine");

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private FavoriteRepository favoriteRepository;

    @Test
    void shouldFavoriteAndRetrieveArticle() {
        // Arrange
        FavoriteRequest request = new FavoriteRequest("article-123", "user-007");

        // Act
        ResponseEntity<FavoriteResponse> response = restTemplate.postForEntity(
                "/api/v4/favorites?apikey=test-api-key",
                request,
                FavoriteResponse.class);

        // Assert (Initial Red Phase - expected to fail or throw 500)
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().articleId()).isEqualTo("article-123");

        // Verify Persistence
        assertThat(favoriteRepository.count()).isEqualTo(1);
    }
}
