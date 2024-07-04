package com.workshop.users.repositories;

import com.workshop.users.api.dto.Product;
import com.workshop.users.exceptions.NotFoundProductException;

import java.util.List;

public interface ProductRepository {
    List<Product> findProductsByIds(List<Long> ids) throws NotFoundProductException;
}
