package com.workshop.users.repositories;

import com.workshop.users.api.dto.Product;
import static org.assertj.core.api.Assertions.*;

import com.workshop.users.exceptions.CantCreateCartException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.client.RestClient;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class CartRepositoryTest {
    RestClient.Builder restClientBuilder;
    private RestClient restClient;
    private CartRepository cartRepository;
    private RestClient.RequestBodyUriSpec request;

    private RestClient.ResponseSpec response;

    @BeforeEach
    void setUp() {
        restClientBuilder = Mockito.mock(RestClient.Builder.class);
        restClient = Mockito.mock(RestClient.class);
        request = Mockito.mock(RestClient.RequestBodyUriSpec.class);
        response = Mockito.mock(RestClient.ResponseSpec.class);
        when(restClientBuilder.build()).thenReturn(restClient);
        cartRepository = new CartRepositoryImpl(restClientBuilder,"createCart");
    }

    @Nested
    @DisplayName("When try to create a cart")
    class CreateCart{

        @Test
        @DisplayName("Given an id Then return true")
        void createCart() {
            when(restClient.post()).thenReturn(request);
            Mockito.when(request.uri(anyString())).thenReturn(request);
            Mockito.when(request.retrieve()).thenReturn(response);
            when(response.onStatus(
                    Mockito.any(),
                    Mockito.any()
            )).thenReturn(response);
            Mockito.when(response.body(boolean.class)).thenReturn(true);
            assertThat(cartRepository.createCart(1L)).isTrue();
        }
        @Test
        @DisplayName("Given an id Then throw the correct exception")
        void createCartThrowException() {
            when(restClient.post()).thenReturn(request);
            Mockito.when(request.uri(anyString())).thenReturn(request);
            Mockito.when(request.retrieve()).thenReturn(response);
            when(response.onStatus(
                    Mockito.any(),
                    Mockito.any()
            )).thenThrow(new CantCreateCartException("Error creating a cart"));
            assertThatThrownBy(()->cartRepository.createCart(1L))
                    .isInstanceOf(CantCreateCartException.class)
                    .hasMessage("Error creating a cart");
        }
    }
}
