package com.workshop.users.model.data;

import com.workshop.users.model.WishProductPK;
import lombok.Generated;

@Generated
public class DataWishProductPK {
    public static final WishProductPK WISH_PRODUCT_PK_USER_1_PRODUCT_1 = new WishProductPK();
    public static final WishProductPK WISH_PRODUCT_PK_USER_1_PRODUCT_2 = new WishProductPK();

    public static final WishProductPK WISH_PRODUCT_PK_USER_1_PRODUCT_3 = new WishProductPK();

    static {
        WISH_PRODUCT_PK_USER_1_PRODUCT_1.setProductId(1L);
        WISH_PRODUCT_PK_USER_1_PRODUCT_1.setUserId(1L);

        WISH_PRODUCT_PK_USER_1_PRODUCT_2.setUserId(1L);
        WISH_PRODUCT_PK_USER_1_PRODUCT_2.setProductId(2L);

        WISH_PRODUCT_PK_USER_1_PRODUCT_3.setUserId(1L);
        WISH_PRODUCT_PK_USER_1_PRODUCT_3.setProductId(3L);
    }

}
