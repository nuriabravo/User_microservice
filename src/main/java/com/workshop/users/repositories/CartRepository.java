package com.workshop.users.repositories;

import com.workshop.users.api.dto.Product;
import com.workshop.users.exceptions.CantCreateCartException;
import com.workshop.users.exceptions.NotFoundProductException;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;

import java.util.List;

public interface CartRepository {
    boolean createCart(Long id) throws CantCreateCartException;
}
