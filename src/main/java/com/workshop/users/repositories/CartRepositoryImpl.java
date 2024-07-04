package com.workshop.users.repositories;

import com.workshop.users.api.dto.Product;
import com.workshop.users.exceptions.CantCreateCartException;
import com.workshop.users.exceptions.NotFoundProductException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Objects;

@Repository
public class CartRepositoryImpl implements CartRepository {

    private RestClient restClient;
    private String createCart;

    public CartRepositoryImpl(@Qualifier("cartRepository") RestClient.Builder restClient, @Value("${cart.create-cart}") String createCart) {
        this.restClient = restClient.build();
        this.createCart = createCart;
    }

    @Override
    @Retryable(retryFor = CantCreateCartException.class, maxAttemptsExpression = "#{@retryConfig.getMaxAttempts()}",
            backoff = @Backoff(delayExpression = "#{@retryConfig.getBackoff()}"))
    public boolean createCart(Long id) throws CantCreateCartException {
        return Objects.requireNonNull(restClient.post()
                .uri(createCart + id)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new CantCreateCartException("Error creating a cart");
                })
                .body(boolean.class));
    }
}
