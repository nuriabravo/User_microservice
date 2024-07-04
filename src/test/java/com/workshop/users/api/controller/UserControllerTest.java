package com.workshop.users.api.controller;

import com.workshop.users.api.controller.Data.DataToUserControllerTesting;
import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.NotFoundAddressException;
import com.workshop.users.exceptions.NotFoundUserException;
import com.workshop.users.exceptions.UserValidationException;
import com.workshop.users.services.address.AddressService;
import com.workshop.users.services.country.CountryService;
import com.workshop.users.services.user.UserService;

import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static com.workshop.users.api.controller.Data.DataToUserControllerTesting.ADDRESS_CALLE_VARAJAS;
import static com.workshop.users.api.controller.Data.DataToUserControllerTesting.USER_ID_2;
import static org.junit.jupiter.api.Assertions.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserControllerTest {
    private UserService userService;
    private UserController userController;
    private AddressService addressService;
    private Validations validations;
    private CountryService countryService;

    @BeforeEach
    void setUp() {
        validations = mock(Validations.class);
        userService  = Mockito.mock(UserService.class);
        addressService = Mockito.mock(AddressService.class);
        countryService = Mockito.mock(CountryService.class);
        userController = new UserController(userService, validations,  addressService, countryService);
    }

    @AfterEach
    void tearDown() {
        DataToUserControllerTesting.USER_ID_2.setEmail("denise@gmail.com");

    }

    @Nested
    @DisplayName("Checking get method")
    class Get {
        @DisplayName("Checking the correct functioning of get method")
        @Order(1)
        @Test
        void getUser() throws NotFoundUserException {

            when(userService.getUserById(2L))
                    .thenReturn( DataToUserControllerTesting.USER_ID_2);
            ResponseEntity<UserDto> responseEntity = userController.getUser(2L);
            UserDto userDto = responseEntity.getBody();
            assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
            assertEquals("Denise", userDto.getName());
            assertNotEquals("Tarraga", userDto.getLastName());
            assertEquals("denise@gmail.com", userDto.getEmail());
            assertEquals("C/VarajasNavaja", userDto.getAddress().getStreet());
            assertNotEquals("41458", userDto.getPhone());
            assertNotEquals("123456789", userDto.getPassword());
            assertEquals(100, userDto.getFidelityPoints());
        }

        @DisplayName("Checking the correct functioning of get method Then throw an exception")
        @Order(2)
        @Test
        void getUserNotFoundExceptions() throws NotFoundUserException {
            when(userService.getUserById(2L)).thenThrow(new NotFoundUserException("Can't found user with this id"));
            assertThatThrownBy(()->userController.getUser(2L))
                    .isInstanceOf(NotFoundUserException.class);
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.OrderAnnotation.class)
    @DisplayName("When try to update a user")
    class PutTest {
        @Test
        @Order(1)
        @DisplayName("Given a valid user")
        void putMappingTest() throws Exception {

            UserDto userDtoChecked = DataToUserControllerTesting.USER_ID_2;
            userDtoChecked.setEmail("paquito@gmail.com");
            AddressDto addressDto = ADDRESS_CALLE_VARAJAS;

            when(validations.checkAllMethods(userDtoChecked))
                    .thenReturn(true);
            when(addressService.updateAddress(3L, userDtoChecked.getAddress()))
                    .thenReturn(ADDRESS_CALLE_VARAJAS);
            when(countryService.getCountryByName(anyString()))
                    .thenReturn(DataToUserControllerTesting.COUNTRY_ESPANYA);
            when(userService.updateUser(2L, userDtoChecked)).thenReturn(userDtoChecked);

            ResponseEntity<UserDto> responseEntity = userController.updateUser(2L, userDtoChecked);
            UserDto userResponse = responseEntity.getBody();

            assertThat(userResponse).isEqualTo(userDtoChecked);
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(responseEntity.getHeaders()).isEmpty();
            verify(validations, times(1)).checkAllMethods(userDtoChecked);
            verify(addressService, times(1)).updateAddress(addressDto.getId(),addressDto);
            verify(userService, times(1)).updateUser(userDtoChecked.getId(), userDtoChecked);
        }

        @Test
        @Order(2)
        @DisplayName("Given an existing user with incorrect values Then return the BAD_REQUEST ")
        void updateUserErrorPasswordTest() throws UserValidationException {
            UserDto userDtoChecked = DataToUserControllerTesting.USER_ID_2;
            userDtoChecked.setPassword("wArong@.com");
            when(validations.checkAllMethods(USER_ID_2))
                    .thenThrow(new UserValidationException( "The password must" +
                            " contain, at least, 8 alphanumeric characters, uppercase, lowercase an special character."));

            assertThatThrownBy(()->userController.updateUser(2L,userDtoChecked))
                    .isInstanceOf(UserValidationException.class)
                            .hasMessage("The password must" +
                                    " contain, at least, 8 alphanumeric characters, " +
                                    "uppercase, lowercase an special character.");

        }
        @Test
        @Order(3)
        @DisplayName("Given an non associated address Then return the NOT_FOUND exception ")
        void updateUserErrorNotFoundAddress() throws UserValidationException {
            UserDto userDtoChecked = DataToUserControllerTesting.USER_ID_2;
            when(validations.checkAllMethods(USER_ID_2)).thenReturn(true);
            when(addressService.updateAddress(userDtoChecked.getAddress().getId(), userDtoChecked.getAddress()))
                    .thenThrow(new NotFoundAddressException( "Address not found"));
            when(countryService.getCountryByName(anyString()))
                    .thenReturn(DataToUserControllerTesting.COUNTRY_ESPANYA);
            //When
            assertThatThrownBy(()->userController.updateUser(2L,userDtoChecked))
                    .isInstanceOf(NotFoundAddressException.class)
                    .hasMessage("Address not found");
        }

        @Test
        @Order(4)
        @DisplayName("Given a non-associated user, then return the NOT_FOUND exception")
        void updateUserErrorNotFoundUser()
                throws UserValidationException, NotFoundUserException {
            UserDto userDtoChecked = DataToUserControllerTesting.USER_ID_2;
            when(validations.checkAllMethods(USER_ID_2)).thenReturn(true);
            when(addressService.updateAddress(userDtoChecked.getAddress().getId(), userDtoChecked.getAddress()))
                    .thenReturn(userDtoChecked.getAddress());
            when(countryService.getCountryByName(anyString()))
                    .thenReturn(DataToUserControllerTesting.COUNTRY_ESPANYA);

            when(userService.updateUser(userDtoChecked.getId(), userDtoChecked))
                    .thenThrow(new NotFoundUserException( "User not found"));

            assertThatThrownBy(()->userController.updateUser(2L,userDtoChecked))
                    .isInstanceOf(NotFoundUserException.class)
                    .hasMessage("User not found");

        }
    }

    @Nested
    @DisplayName("When try to increment fidelity")
    class FidelityPoints {
        @Test
        @DisplayName("Given a good id user and fidelity points to increment " +
                "then return a response entity with the user modify")
        void incrementFidelityPointsTest() throws NotFoundUserException {
            when(userService.updateFidelityPoints(anyLong(), anyInt()))
                    .thenReturn(USER_ID_2);
            ResponseEntity<UserDto> responseEntity = userController.incrementFidelityPoints(1L, 1);

            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            assertThat(responseEntity.getBody()).isEqualTo(USER_ID_2);
        }

        @Test
        @DisplayName("Given a non existed id user and fidelity points to increment " +
                "then throw a NotFoundUserException")
        void incrementFidelityPointsTestError() throws NotFoundUserException {
            when(userService.updateFidelityPoints(anyLong(), anyInt()))
                    .thenThrow(new NotFoundUserException("The user with this id not exists"));

            assertThatThrownBy(() -> userController.incrementFidelityPoints(1L, 1))
                    .isInstanceOf(NotFoundUserException.class)
                    .hasMessage("The user with this id not exists");

        }
    }

 }