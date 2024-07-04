package com.workshop.users.repositories;

import com.workshop.users.api.dto.Product;
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
public class ProductRepositoryImpl implements ProductRepository{
    private final RestClient restClient;

    private String routeByIds;


    public ProductRepositoryImpl(@Qualifier("productRepository") RestClient.Builder builder, @Value("${catalog.products-by-ids}") String routeByIds) {
        this.restClient =  builder.build();
        this.routeByIds = routeByIds;
    }


    @Override
    @Retryable(
            retryFor = NotFoundProductException.class,
            maxAttemptsExpression = "#{@retryConfig.getMaxAttempts()}",
            backoff = @Backoff(delayExpression = "#{@retryConfig.getBackoff()}")
    )
    public List<Product> findProductsByIds(List<Long> ids)
            throws NotFoundProductException  {

        return List.of(Objects.requireNonNull(restClient.post()
                .uri(routeByIds)
                .contentType(MediaType.APPLICATION_JSON)
                .body(ids)
                .retrieve()
                .onStatus(HttpStatusCode::isError, (request, response) -> {
                    throw new NotFoundProductException("Can't found the id of one product");
                })
                .body(Product[].class)));
    }


}
