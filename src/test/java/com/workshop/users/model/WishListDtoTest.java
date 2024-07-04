package com.workshop.users.model;

import com.workshop.users.api.dto.WishListDto;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WishListDtoTest {


    @Test
    @DisplayName("Given a userid and product id " +
            "When getEntity WishListDto" +
            "Then return WishPoductEntity")
    void getEntity(){
        WishProductEntity wishProductEntity = new WishProductEntity();
        WishProductPK wishProductPK = new WishProductPK();
        wishProductPK.setUserId(1L);
        wishProductPK.setProductId(2L);
        wishProductEntity.setWishProductPK(wishProductPK);
        assertThat(WishListDto.getEntity(1L,2L))
                .isInstanceOf(WishProductEntity.class)
                .isEqualTo(wishProductEntity);
    }


}
