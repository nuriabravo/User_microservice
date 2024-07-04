package com.workshop.users.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.web.client.RestClient;

@Configuration
@EnableRetry
public class ProductWebClientConfiguration {
    @Value("${catalog.url}")
    private String PRODUCT_URL;
    @Value("${cart.url}")
    private String CART_URL;

    @Bean(name = "productRepository")
    public RestClient.Builder restClientBuilder(){
        return RestClient.builder().baseUrl(PRODUCT_URL);
    }
    @Bean(name = "cartRepository")
    public RestClient.Builder restClientCartBuilder(){
        return RestClient.builder().baseUrl(CART_URL);
    }
}
