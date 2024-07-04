package com.workshop.users.services.Address;

import com.workshop.users.api.controller.Data.DataInitzializerController;
import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.exceptions.AddressServiceException;
import com.workshop.users.exceptions.NotFoundAddressException;
import com.workshop.users.model.AddressEntity;
import com.workshop.users.repositories.AddressDAORepository;
import com.workshop.users.services.address.AddressService;
import com.workshop.users.services.address.AddressServiceImpl;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AddressServiceImplTest {

    private AddressDAORepository addressDAORepository;
    private AddressService addressService;

    private AddressDto addressDto;
    private AddressEntity addressEntityWithId;

    @BeforeEach
    void setUp() {
        addressDAORepository = mock(AddressDAORepository.class);
        addressService = new AddressServiceImpl(addressDAORepository);
        addressDto = AddressDto.builder()
                .id(null)
                .door("1A")
                .number(12)
                .cityName("Madrid")
                .zipCode("36458")
                .street("C/ Las Ramblas")
                .build();
        addressEntityWithId = new AddressEntity();
        addressEntityWithId.setId(1L);
        addressEntityWithId.setDoor("1A");
        addressEntityWithId.setNumber(12);
        addressEntityWithId.setCityName("Madrid");
        addressEntityWithId.setZipCode("36458");
        addressEntityWithId.setStreet("C/ Las Ramblas");
    }

    @Test
    @DisplayName("When try to add Address then return the correct address")
    void addAddress()  {
        when(addressDAORepository.save(any(AddressEntity.class))).thenReturn(addressEntityWithId);
        assertThat(addressService.addAddress(addressDto)).isEqualTo(AddressEntity.fromEntity(addressEntityWithId));
    }

    @Nested
    @DisplayName("When try to get Address")
    class GetAddress {
        @Test
        @DisplayName("Given a valid Id then return a user")
        void getAddress() {
            when(addressDAORepository.findById(anyLong())).thenReturn(Optional.of(addressEntityWithId));
            assertThat(addressService.getAddressById(1L)).isEqualTo(AddressEntity.fromEntity(addressEntityWithId));
        }


        @Test

        @DisplayName("Given a null Id then throw an error")
        void getAddressErrorIdNull() {
            assertThatThrownBy(() -> addressService.getAddressById(null))
                    .isInstanceOf(NotFoundAddressException.class)
                    .hasMessage("Request not valid. The address with the id null is null");
        }

        @Test
        @DisplayName("Add address with a null Id then throw an error")
        void getAddressErrorNotExistUser() {
            when(addressDAORepository.findById(anyLong())).thenReturn(Optional.empty());
            assertThatThrownBy(() -> addressService.getAddressById(1L))
                    .isInstanceOf(RuntimeException.class);
        }

        @Test
        @DisplayName("Given a null Id then throw an error")
        void addAddressErrorIdNull() {
            assertThatThrownBy(() -> addressService.getAddressById(null))
                    .isInstanceOf(NotFoundAddressException.class)
                    .hasMessage("Request not valid. The address with the id null is null");
        }

        @Test
        @DisplayName("Given an existing Id then throw a RegisterException")
        void addAddressWithExistingId() {
                AddressDto addressWithExistingId = DataInitzializerController.ADDRESS_VALLECAS;
            addressWithExistingId.setId(1L);
            when(addressDAORepository.findById(addressWithExistingId.getId())).thenReturn(Optional.of(new AddressEntity()));

            assertThatThrownBy(() -> addressService.addAddress(addressWithExistingId))
                    .isInstanceOf(AddressServiceException.class)
                    .hasMessage("You can't add an address with id");
        }

        @Nested
        @DisplayName("When try to updated Address")
        class updateAddress {
            @Test
            @DisplayName("Given a address to change then return the address updated")
            void updateAddressTest()  {
                addressDto.setId(1L);
                when(addressDAORepository.findById(anyLong())).thenReturn(Optional.of(addressEntityWithId));
                when(addressDAORepository.save(any(AddressEntity.class))).thenReturn(addressEntityWithId);
                assertThat(addressService.updateAddress(addressDto.getId(), addressDto)).isEqualTo(addressDto);
            }

            @Test
            @DisplayName("Given a address to change that not exists then throw not found address exception")
            void updateAddressThrowError() {
                addressDto.setId(1L);
                when(addressDAORepository.findById(anyLong())).thenReturn(Optional.empty());
                when(addressDAORepository.save(any(AddressEntity.class))).thenReturn(addressEntityWithId);
                assertThatThrownBy(()->addressService.updateAddress(1L,addressDto))
                        .isInstanceOf(NotFoundAddressException.class)
                        .hasMessage("The address with the id 1 was not found.");
            }
        }
    }
}
