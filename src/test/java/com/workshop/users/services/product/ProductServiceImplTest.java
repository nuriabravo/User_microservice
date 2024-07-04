package com.workshop.users.services.product;

import com.workshop.users.api.dto.Product;
import com.workshop.users.exceptions.NotFoundProductException;
import com.workshop.users.repositories.ProductRepository;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.List;


class ProductServiceImplTest {

    private ProductRepository productRepository;
    private ProductService productService;
    private List<Product> product;

    private List<Long> ids;

    @BeforeEach
    void setUp() {
        productRepository = Mockito.mock(ProductRepository.class);
        productService = new ProductServiceImpl(productRepository);
        product = List.of(Product.builder()
                .id(2L)
                .name("ToyStory toy")
                .categoryId(3)
                .currentStock(200)
                .description("The best toy for your children")
                .price(10D)
                .minStock(100)
                .weight(0.5D)
                .build(),
                Product.builder()
                        .id(1L)
                        .name("ToyStory toy")
                        .categoryId(3)
                        .currentStock(200)
                        .description("The best toy for your children")
                        .price(10D)
                        .minStock(100)
                        .weight(0.5D)
                        .build());

        ids = List.of(1L,2L);
    }


    @Nested
    @DisplayName("When find product by Id")
    class FindProductById{

        @Test
        @DisplayName("Given a valid Ids " +
                "Then return the associated Products")
        void findProductByIdGood() throws NotFoundProductException {
            when(productRepository.findProductsByIds(ids))
                    .thenReturn(product);

            assertThat(productService.findProductsByIds(ids))
                    .isEqualTo(product);

        }
        @Test
        @DisplayName("Given a invalid Ids " +
                "Then throw not found product exception")
        void findProductByIdInvalid() {
            when(productRepository.findProductsByIds(ids))
                    .thenThrow(new NotFoundProductException("Not found product"));

            assertThatThrownBy(() ->
                productService.findProductsByIds(ids))
            .isInstanceOf(NotFoundProductException.class);
        }
    }
}