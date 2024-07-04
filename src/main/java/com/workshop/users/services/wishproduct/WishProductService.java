package com.workshop.users.services.wishproduct;

import com.workshop.users.exceptions.ConflictWishListException;
import com.workshop.users.exceptions.NotFoundWishProductException;
import com.workshop.users.model.WishProductEntity;

public interface WishProductService {


    Long addWishProducts(WishProductEntity wishProductEntity) throws ConflictWishListException;

    void deleteWishProducts(WishProductEntity wishProductEntity) throws NotFoundWishProductException;
}
