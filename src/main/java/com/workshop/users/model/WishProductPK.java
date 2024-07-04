package com.workshop.users.model;

import jakarta.persistence.Embeddable;
import lombok.Data;

import java.io.Serializable;
@Embeddable
@Data
public class WishProductPK implements Serializable {
    private Long userId;
    private Long productId;
}
