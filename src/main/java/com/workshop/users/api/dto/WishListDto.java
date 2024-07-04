package com.workshop.users.api.dto;

import com.workshop.users.model.WishProductEntity;
import com.workshop.users.model.WishProductPK;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.*;

@Data
@Builder
public class WishListDto implements Serializable {
    private Long userId;
    private Set<Long> productsIds;



    public static WishProductEntity getEntity(Long userId,Long productId){
        WishProductEntity wishProductEntity = new WishProductEntity();
        WishProductPK wishProductPK = new WishProductPK();
        wishProductPK.setUserId(userId);
        wishProductPK.setProductId(productId);
        wishProductEntity.setWishProductPK(wishProductPK);
        return wishProductEntity;
    }




}
