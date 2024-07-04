package com.workshop.users.model;

import com.workshop.users.api.dto.WishListDto;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.List;


class WishProductEntityTest {

    @Test
    @DisplayName("Given a WishProductEntity when from entity then return a WishListDto")
    void fromEntity() {
        //Given
        WishProductEntity wishProductEntity = new WishProductEntity();
        WishProductPK wishProductPK = new WishProductPK();
        WishListDto wishListDto = WishListDto.builder().userId(1L).productsIds(new HashSet<>(List.of(1L))).build();
        WishListDto wishListDtoOther = WishListDto.builder().userId(1L).productsIds(new HashSet<>(List.of(2L))).build();
        wishProductPK.setProductId(1L);
        wishProductPK.setUserId(1L);
        wishProductEntity.setWishProductPK(wishProductPK);

        //When and Then
        Assertions.assertThat(wishProductEntity.fromEntity())
                .isEqualTo(wishListDto)
                .isNotEqualTo(wishListDtoOther);
    }
}