package com.workshop.users.services.wishproduct;

import com.workshop.users.exceptions.ConflictWishListException;
import com.workshop.users.exceptions.NotFoundWishProductException;
import com.workshop.users.model.WishProductEntity;
import com.workshop.users.model.WishProductPK;
import com.workshop.users.repositories.WishProductDAORepository;
import static org.assertj.core.api.Assertions.*;

import jakarta.persistence.EntityExistsException;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;


import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class WishProductServiceImplTests {
    private WishProductDAORepository wishProductDAORepository;
    private WishProductService wishProductService;
    private WishProductEntity wishProductEntity;
    WishProductPK wishProductPK;

    @BeforeEach
    void setUp() {
        wishProductDAORepository = Mockito.mock(WishProductDAORepository.class);
        wishProductService = new WishProductServiceImpl(wishProductDAORepository);
        wishProductEntity = new WishProductEntity();
        wishProductPK = new WishProductPK();
        wishProductPK.setProductId(1L);
        wishProductPK.setUserId(1L);
        wishProductEntity.setWishProductPK(wishProductPK);
    }



    @Nested
    @DisplayName("When add WishProduct")
    class AddWishProduct{
        @Test
        @DisplayName("Given wishList to add " +
                "Then return the same wishList")
        void addWishList() throws ConflictWishListException {
            //Given
            when(wishProductDAORepository.findById(any(WishProductPK.class)))
                    .thenReturn(Optional.empty());
            when(wishProductDAORepository.save(wishProductEntity))
                    .thenReturn(wishProductEntity);
            //When and Then
            assertThat(wishProductService.addWishProducts(wishProductEntity))
                    .isEqualTo(1L);
        }
        @Test
        @DisplayName("Given wish list with a product that already is wished " +
                "Then throw Exception")
        void addWishListThrowError() {
            //Given
            when(wishProductDAORepository.findById(any(WishProductPK.class)))
                    .thenReturn(Optional.of(wishProductEntity));
            when(wishProductDAORepository.save(wishProductEntity))
                    .thenThrow(new EntityExistsException());
            //When and Then
            assertThatThrownBy(() ->
                wishProductService.addWishProducts(wishProductEntity))
            .isInstanceOf(ConflictWishListException.class);
        }
    }

    @Nested
    @DisplayName("When delete WishProduct")
    class DeleteWishProduct{
        @Test
        @DisplayName("Given wish product to delete " +
                "Then delete the item")
        void addWishList() throws NotFoundWishProductException {
            //Given
            doNothing().when(wishProductDAORepository)
                        .delete(wishProductEntity);
            when(wishProductDAORepository.findById(any(WishProductPK.class)))
                    .thenReturn(Optional.of(wishProductEntity));
            //When and Then
            wishProductService.deleteWishProducts(wishProductEntity);

            verify(wishProductDAORepository).delete(wishProductEntity);
        }

        @Test
        @DisplayName("Given wish product that not exists " +
                "Then throw NotFoundWishProductException")
        void addWishListThrowError() {
            //Given
            when(wishProductDAORepository.findById(any(WishProductPK.class)))
                    .thenReturn(Optional.empty());
            //When and Then
            assertThatThrownBy(() ->
                wishProductService.deleteWishProducts(wishProductEntity))
            .isInstanceOf(NotFoundWishProductException.class);
        }
    }
}
