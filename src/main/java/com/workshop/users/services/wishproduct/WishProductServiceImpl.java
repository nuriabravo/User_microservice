package com.workshop.users.services.wishproduct;

import com.workshop.users.exceptions.ConflictWishListException;
import com.workshop.users.exceptions.NotFoundWishProductException;
import com.workshop.users.model.WishProductEntity;
import com.workshop.users.repositories.WishProductDAORepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class WishProductServiceImpl implements WishProductService {
    private final WishProductDAORepository wishProductDAORepository;


    @Override
    public Long addWishProducts(WishProductEntity wishProductEntity) throws ConflictWishListException {

        if (wishProductDAORepository.findById(wishProductEntity.getWishProductPK()).isPresent())
            throw new ConflictWishListException("The user with id " +
                    wishProductEntity.getWishProductPK().getUserId() +
                    " already have the product with id " +
                    wishProductEntity.getWishProductPK().getProductId() +
                    " in wishes");
        else
            return wishProductDAORepository.save(wishProductEntity)
                    .getWishProductPK().getProductId();

    }

    @Override
    public void deleteWishProducts(WishProductEntity wishProductEntity) throws NotFoundWishProductException {
        if (wishProductDAORepository.findById(wishProductEntity.getWishProductPK()).isPresent())
            wishProductDAORepository.delete(wishProductEntity);
        else
            throw new NotFoundWishProductException("The product with id "
                    + wishProductEntity.getWishProductPK().getProductId()
                    + " is not in your wishes");
    }
}
