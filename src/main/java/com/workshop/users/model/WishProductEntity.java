package com.workshop.users.model;

import com.workshop.users.api.dto.WishListDto;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;

@Entity
@Table(name="WISHES")
@Data
public class WishProductEntity implements Serializable {
    @EmbeddedId
    private WishProductPK wishProductPK;

    public WishListDto fromEntity(){
        return WishListDto.builder()
                .userId(getWishProductPK().getUserId())
                .productsIds(new HashSet<>(Collections.singletonList(getWishProductPK().getProductId())))
                .build();
    }
}
