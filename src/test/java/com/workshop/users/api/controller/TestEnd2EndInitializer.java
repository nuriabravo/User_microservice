package com.workshop.users.api.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.workshop.users.api.dto.*;
import com.workshop.users.exceptions.MyResponseError;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.reactive.server.WebTestClient;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class TestEnd2EndRegisterTest {
    @Autowired
    WebTestClient webTestClient;
    private static ObjectMapper objectMapper;
    private static MockWebServer mockWebServer;

    @BeforeAll
    static void beforeAll() throws IOException {
        objectMapper = new ObjectMapper();
        mockWebServer = new MockWebServer();
        mockWebServer.start(8081);
    }

    @AfterAll
    static void afterAll() throws IOException {
        mockWebServer.close();
    }

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Nested
    @DisplayName("Register")
    class Register {

        @Test
        @DisplayName("Given correct credentials a user is registered. Then return that user")
        void registerUser() throws JsonProcessingException {
            // Given
            UserDto newUser = UserDto.builder()
                    .name("Aria")
                    .lastName("Fei")
                    .email("aria@example.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(0)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .name("España")
                            .tax(21f)
                            .prefix("+34")
                            .timeZone("Europe/Madrid")
                            .build())
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(true))
                    .setStatus("HTTP/1.1 201 Created")
                    .setHeader("Content-Type", "application/json"));

            // When
            webTestClient.post()
                    .uri("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(UserDto.class)
                    .value(userDto -> {
                        // Then
                        assertThat(passwordEncoder.matches(newUser.getPassword(), userDto.getPassword())).isTrue();
                        assertThat(userDto.getName()).isEqualTo(newUser.getName());
                        assertThat(userDto.getLastName()).isEqualTo(newUser.getLastName());
                        assertThat(userDto.getEmail()).isEqualTo(newUser.getEmail());
                        assertThat(userDto.getBirthDate()).isEqualTo(newUser.getBirthDate());
                        assertThat(userDto.getFidelityPoints()).isEqualTo(newUser.getFidelityPoints());
                        assertThat(userDto.getPhone()).isEqualTo(newUser.getPhone());

                        assertThat(userDto.getAddress().getCityName()).isEqualTo(newUser.getAddress().getCityName());
                        assertThat(userDto.getAddress().getZipCode()).isEqualTo(newUser.getAddress().getZipCode());
                        assertThat(userDto.getAddress().getStreet()).isEqualTo(newUser.getAddress().getStreet());
                        assertThat(userDto.getAddress().getNumber()).isEqualTo(newUser.getAddress().getNumber());
                        assertThat(userDto.getAddress().getDoor()).isEqualTo(newUser.getAddress().getDoor());

                        assertThat(userDto.getCountry().getName()).isEqualTo(newUser.getCountry().getName());
                        assertThat(userDto.getCountry().getTax()).isEqualTo(newUser.getCountry().getTax());
                        assertThat(userDto.getCountry().getPrefix()).isEqualTo(newUser.getCountry().getPrefix());
                        assertThat(userDto.getCountry().getTimeZone()).isEqualTo(newUser.getCountry().getTimeZone());
                    });
        }

        @Test
        @DisplayName("Given correct credentials a user is registered but have problems creating the cart." +
                " Then throw my exception")
        void registerUserCartError() throws JsonProcessingException {
            // Given
            UserDto newUser = UserDto.builder()
                    .name("Aria")
                    .lastName("Fei")
                    .email("aria45@example.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(0)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .name("España")
                            .tax(21f)
                            .prefix("+34")
                            .timeZone("Europe/Madrid")
                            .build())
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Failed to conect\"}")
                    .setStatus("HTTP/1.1 500 Internal Server Error")
                    .setHeader("Content-Type", "application/json"));
            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Failed to conect\"}")
                    .setStatus("HTTP/1.1 500 Internal Server Error")
                    .setHeader("Content-Type", "application/json"));
            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Failed to conect\"}")
                    .setStatus("HTTP/1.1 500 Internal Server Error")
                    .setHeader("Content-Type", "application/json"));

            // When
            webTestClient.post()
                    .uri("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .exchange()
                    .expectStatus().is5xxServerError()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError -> {
                        assertThat(myResponseError)
                                .hasFieldOrPropertyWithValue("code",HttpStatus.INTERNAL_SERVER_ERROR)
                                .hasFieldOrPropertyWithValue("message","Error creating a cart");

                    });
        }

        @Test
        @DisplayName("Given correct credentials but Put Fail Country. Then throw an countryError")
        void registerUserWithWrongCountry() {
            // Given
            UserDto newUser = UserDto.builder()
                    .name("Aria")
                    .lastName("Fei")
                    .email("manolita@example.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(40)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .name("Paco")
                            .build())
                    .build();

            // When
            webTestClient.post()
                    .uri("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError ->
                        assertThat(myResponseError.getCode()).isEqualTo(HttpStatus.NOT_FOUND));
        }


        @Test
        @DisplayName("Given incorrect credentials. Then throw an registerException")
        void registerUserWitRegisterError() {
            // Given
            UserDto newUser = UserDto.builder()
                    .id(1L)
                    .name("Aria")
                    .lastName("Fei")
                    .email("aria3@example.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(40)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .build())
                    .country(CountryDto.builder()
                            .name("España")
                            .build())
                    .build();

            // When
            webTestClient.post()
                    .uri("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError ->
                        assertThat(myResponseError.getCode()).isEqualTo(HttpStatus.BAD_REQUEST));
        }

        @Test
        @DisplayName("Given incorrect credentials a user can't be registered. Then return BAD_REQUEST status")
        void invalidUserRegister() {
            //Given
            UserDto invalidUser = UserDto.builder()
                    .name("Aria")
                    .lastName("Fei")
                    .email("ariaexample.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(40)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .name("España")
                            .tax(21f)
                            .prefix("+34")
                            .timeZone("Europe/Madrid")
                            .build())
                    .build();
            //When
            webTestClient.post()
                    .uri("/register")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(invalidUser)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError ->
                        assertThat(myResponseError.getCode()).isEqualTo(HttpStatus.BAD_REQUEST));
        }
    }


    @Nested
    @DisplayName("Login")
    class TestEnd2EndLoginTest {


        @Test
        @DisplayName("Given an logged user when call to login endpoint Then return associated user")
        void postLogin() {
            //Given
            UserDto expectedUserDto = UserDto.builder()
                    .id(1L)
                    .name("Juan")
                    .lastName("García")
                    .email("juangarcia@example.com")
                    .password("$2a$10$OyJUHBSm0sU8eF8os0ZuoOwDRmgg8ns4owWtIXItlYmN.1pDVxve6")
                    .fidelityPoints(150)
                    .birthDate("1990/01/01")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .id(1L)
                            .cityName("Madrid")
                            .zipCode("47562")
                            .street("C/ La Coma")
                            .number(32)
                            .door("1A")
                            .build())
                    .country(CountryDto.builder()
                            .id(1L)
                            .name("España")
                            .tax(21f)
                            .prefix("+34")
                            .timeZone("Europe/Madrid")
                            .build())
                    .build();
            Login validLogin = Login.builder()
                    .email("juangarcia@example.com")
                    .password("1234")
                    .build();
            //When
            webTestClient.post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(validLogin)
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .value(userDto -> {
                        //Then
                        assertThat(userDto).isEqualTo(expectedUserDto);
                    });
        }

        @Test
        @DisplayName("Given an bad email user when call to login endpoint Then return uauthorized status")
        void postLoginIncorrectEmail() {
            //Given
            Login validLogin = Login.builder()
                    .email("juangarcio@example.com")
                    .password("1234")
                    .build();
            //When
            webTestClient.post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(validLogin)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(exception -> {
                        //Then
                        assertThat(exception).isEqualTo(MyResponseError.builder()
                                .code(HttpStatus.NOT_FOUND)
                                .message("The email or the password are incorrect.")
                                .build());
                    });
        }

        @Test
        @DisplayName("Given an bad password user when call to login endpoint Then return uauthorized status")
        void postLoginIncorrectPassword() {
            //Given
            Login validLogin = Login.builder()
                    .email("juangarcia@example.com")
                    .password("1235744")
                    .build();
            //When
            webTestClient.post()
                    .uri("/login")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(validLogin)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(exception -> {
                        //Then
                        assertThat(exception).isEqualTo(MyResponseError.builder()
                                .code(HttpStatus.NOT_FOUND)
                                .message("The email or the password are incorrect.")
                                .build());
                    });
        }
    }

    @Nested
    @DisplayName("Get user by Id")
    class TestEnd2EndGetUserTest {

        @Test
        @DisplayName("Given an  associated user id when call to get users endpoint Then return the correct user")
        void getUserById() {

            //When
            webTestClient.get()
                    .uri("/users/1")
                    .exchange()
                    .expectStatus().isOk()
                    .expectBody(UserDto.class)
                    .value(userDto -> {
                        //Then
                        assertThat(userDto.getName()).isEqualTo("Juan");
                        assertThat(userDto)
                                .hasFieldOrPropertyWithValue("email", "juangarcia@example.com")
                                .hasFieldOrPropertyWithValue("name", "Juan")
                                .hasFieldOrPropertyWithValue("lastName", "García");
                    });
        }


        @Test
        @DisplayName("Given a non associated user id when call to get users endpoint Then throw not found exceptions")
        void getUserByIdNonAssociatedId() {

            //When
            webTestClient.get()
                    .uri("/users/5")
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(exception -> {
                        //Then
                        assertThat(exception).isEqualTo(MyResponseError.builder()
                                .code(HttpStatus.NOT_FOUND)
                                .message("Not found user")
                                .build());
                    });
        }
    }



    @Nested
    @DisplayName("Put user by Id")
    class TestEnd2EndPutUserTest {
        private final UserDto newUser;
        public TestEnd2EndPutUserTest(){
            newUser = UserDto.builder()
                    .id(2L)
                    .name("Aria")
                    .lastName("Fei")
                    .email("ramoncita@example.com")
                    .password("Ar1a@31234.")
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .id(2L)
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .id(2L)
                            .name("Estonia")
                            .build())
                    .build();
        }

        @Test
        @DisplayName("Given an associated user id " +
                "when call to put users endpoint " +
                "Then return the user updated")
        void putUserById() {

            //When
            webTestClient.put()
                    .uri("/users/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(newUser)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(UserDto.class)
                    .value(userDto -> {
                        // Then
                        assertThat(passwordEncoder.matches(newUser.getPassword(), userDto.getPassword())).isTrue();
                        assertThat(userDto.getName()).isEqualTo(newUser.getName());
                        assertThat(userDto.getLastName()).isEqualTo(newUser.getLastName());
                        assertThat(userDto.getEmail()).isEqualTo(newUser.getEmail());
                        assertThat(userDto.getBirthDate()).isEqualTo(newUser.getBirthDate());
                        assertThat(userDto.getPhone()).isEqualTo(newUser.getPhone());

                        assertThat(userDto.getAddress().getCityName()).isEqualTo(newUser.getAddress().getCityName());
                        assertThat(userDto.getAddress().getZipCode()).isEqualTo(newUser.getAddress().getZipCode());
                        assertThat(userDto.getAddress().getStreet()).isEqualTo(newUser.getAddress().getStreet());
                        assertThat(userDto.getAddress().getNumber()).isEqualTo(newUser.getAddress().getNumber());
                        assertThat(userDto.getAddress().getDoor()).isEqualTo(newUser.getAddress().getDoor());
                        assertThat(userDto.getCountry().getId()).isEqualTo(2L);
                    });
        }


        @Test
        @DisplayName("Given a non associated user id " +
                "when call to put users endpoint " +
                "Then throw not found user exceptions")
        void putUserByIdNonAssociatedUserId() {
            UserDto userWithNoExistingId = UserDto.builder()
                    .id(9999999L)
                    .name("Aria")
                    .lastName("Fei")
                    .email("alfonsita@example.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(40)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .id(2L)
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .id(2L)
                            .name("Estonia")
                            .build())
                    .build();
            //When
            webTestClient.put()
                    .uri("/users/999999")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userWithNoExistingId)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(exception -> {
                        //Then
                        assertThat(exception).isEqualTo(MyResponseError.builder()
                                .code(HttpStatus.NOT_FOUND)
                                .message("The user with the id 999999 was not found.")
                                .build());
                    });
        }

        @Test
        @DisplayName("Given a password with wrong format " +
                "when call to put users endpoint " +
                "Then throw bad request exceptions")
        void putUserByIdBadRequest() {
            UserDto userWithBadCredentials = UserDto.builder()
                    .id(2L)
                    .name("Aria")
                    .lastName("Fei")
                    .email("aria2@example.com")
                    .password("ar1a@31234.")
                    .fidelityPoints(40)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .build();
            //When
            webTestClient.put()
                    .uri("/users/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userWithBadCredentials)
                    .exchange()
                    .expectStatus().isBadRequest()
                    .expectBody(MyResponseError.class)
                    .value(exception -> {
                        //Then
                        assertThat(exception).isEqualTo(MyResponseError.builder()
                                .code(HttpStatus.BAD_REQUEST)
                                .message("The password must contain, at least," +
                                        " 8 alphanumeric characters, uppercase, " +
                                        "lowercase an special character.")
                                .build());
                    });
        }

        @Test
        @DisplayName("Given a non associated address id " +
                "when call to put users endpoint " +
                "Then throw not found address exceptions")
        void putUserByIdNonAssociatedAddressId() {
            UserDto userWithBadCredentials = UserDto.builder()
                    .id(2L)
                    .name("Aria")
                    .lastName("Fei")
                    .email("aria2@example.com")
                    .password("Ar1a@31234.")
                    .fidelityPoints(40)
                    .birthDate("1994/04/14")
                    .phone("123456789")
                    .address(AddressDto.builder()
                            .id(999999L)
                            .cityName("Valencia")
                            .zipCode("46360")
                            .street("C/ La Calle")
                            .number(32)
                            .door("2A")
                            .build())
                    .country(CountryDto.builder()
                            .id(2L)
                            .build())
                    .build();
            //When
            webTestClient.put()
                    .uri("/users/2")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(userWithBadCredentials)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(exception -> {
                        //Then
                        assertThat(exception).isEqualTo(MyResponseError.builder()
                                .code(HttpStatus.NOT_FOUND)
                                .message("The address with the id 999999 was not found.")
                                .build());
                    });
        }

    }



    @Nested
    @DisplayName("Post WishList ")
    class TestEnd2EndPostWishList {

        @Test
        @DisplayName("Given a good Wish List When post wish list Then return the same wishlist")
        void postWishList() throws JsonProcessingException {
            //Given
            WishListDto wishListDto = WishListDto.builder()
                    .userId(1L)
                    .productsIds(new HashSet<>(List.of(1L, 2L, 3L)))
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(List.of(Product.builder()
                                    .id(1L)
                                    .build(),
                            Product.builder()
                                    .id(2L)
                                    .build(),
                            Product.builder()
                                    .id(3L)
                                    .build())))
                    .setHeader("Content-Type", "application/json"));

            //When
            webTestClient.post()
                    .uri("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wishListDto)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(WishListDto.class)
                    .value(wishListDtoResponse -> {
                        //Then
                        assertThat(wishListDtoResponse).isEqualTo(wishListDto);
                    });

        }


        @Test
        @DisplayName("Given a Wish List but the user not exists When post wish list Then throw not found exception")
        void postWishListNotFoundUserException() throws JsonProcessingException {
            //Given
            WishListDto wishListDto = WishListDto.builder()
                    .userId(3L)
                    .productsIds(new HashSet<>(List.of(1L, 2L, 3L)))
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(List.of(Product.builder()
                                    .id(1L)
                                    .build(),
                            Product.builder()
                                    .id(2L)
                                    .build(),
                            Product.builder()
                                    .id(3L)
                                    .build())))
                    .setHeader("Content-Type", "application/json"));

            //When
            webTestClient.post()
                    .uri("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wishListDto)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError -> {
                        //Then
                        assertThat(myResponseError.getMessage()).isEqualTo("Not found user");
                    });
        }

        @Test
        @DisplayName("Given a Wish List but the products not exists When post wish list Then throw not found exception")
        void postWishListNotFoundProductException() {
            //Given
            WishListDto wishListDto = WishListDto.builder()
                    .userId(1L)
                    .productsIds(new HashSet<>(List.of(9999L, 3333L, 55555L)))
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Not found product with this ids\"}")
                    .setStatus("HTTP/1.1 404 Not Found")
                    .setHeader("Content-Type", "application/json"));
            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Not found product with this ids\"}")
                    .setStatus("HTTP/1.1 404 Not Found")
                    .setHeader("Content-Type", "application/json"));
            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Not found product with this ids\"}")
                    .setStatus("HTTP/1.1 404 Not Found")
                    .setHeader("Content-Type", "application/json"));
            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Not found product with this ids\"}")
                    .setStatus("HTTP/1.1 404 Not Found")
                    .setHeader("Content-Type", "application/json"));


            //When
            webTestClient.post()
                    .uri("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wishListDto)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError -> {
                        //Then
                        assertThat(myResponseError.getMessage()).isEqualTo("Can't found the id of one product");
                    });
        }

        @Test
        @DisplayName("Given a Wish List but the products not exists When post wish " +
                "list Then throw not found exception")
        void postWishListNotFoundProductExceptionRetry() throws JsonProcessingException {
            //Given
            WishListDto wishListDto = WishListDto.builder()
                    .userId(1L)
                    .productsIds(new HashSet<>(List.of(9L, 10L, 11L)))
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Failed to conect\"}")
                    .setStatus("HTTP/1.1 500 Internal Server Error")
                    .setHeader("Content-Type", "application/json"));
            mockWebServer.enqueue(new MockResponse()
                    .setBody("{\"message\":\"Failed to conect\"}")
                    .setStatus("HTTP/1.1 500 Internal Server Error")
                    .setHeader("Content-Type", "application/json"));

            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(List.of(Product.builder()
                                    .id(1L)
                                    .build(),
                            Product.builder()
                                    .id(2L)
                                    .build(),
                            Product.builder()
                                    .id(3L)
                                    .build())))
                            .setStatus("HTTP/1.1 200 OK")
                    .setHeader("Content-Type", "application/json"));

            //When
            webTestClient.post()
                    .uri("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wishListDto)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(WishListDto.class)
                    .value(wishListDtoResponse -> {
                        //Then
                        assertThat(wishListDtoResponse).isEqualTo(wishListDto);
                    });
        }

        @Test
        @DisplayName("Given a Wish List but the products is already in  wishes of the user When post wish list Then throw not found exception")
        void postWishListConflictWishListException() throws JsonProcessingException {
            //Given
            WishListDto wishListDto = WishListDto.builder()
                    .userId(1L)
                    .productsIds(new HashSet<>(List.of(1L, 2L, 8L)))
                    .build();

            mockWebServer.enqueue(new MockResponse()
                    .setBody(objectMapper.writeValueAsString(List.of(Product.builder()
                                    .id(1L)
                                    .build(),
                            Product.builder()
                                    .id(2L)
                                    .build(),
                            Product.builder()
                                    .id(3L)
                                    .build())))
                    .setHeader("Content-Type", "application/json"));

            //When
            webTestClient.post()
                    .uri("/wishlist")
                    .contentType(MediaType.APPLICATION_JSON)
                    .bodyValue(wishListDto)
                    .exchange()
                    .expectStatus().is4xxClientError()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError -> {
                        //Then
                        assertThat(myResponseError.getMessage()).isEqualTo("The user with id 1 already have the product with id 1 in wishes");
                    });


        }
    }

    @Nested
    @DisplayName("Delete WishList ")
    class TestEnd2EndDeleteWishList {

        @Test
        @DisplayName("Given an user id an product When delete wish product Then delete wish product")
        void deleteWishList()  {
            //When
            webTestClient.delete()
                    .uri("/wishlist/1/8")
                    .exchange()
                    .expectStatus().isNoContent();

        }


        @Test
        @DisplayName("Given a user id and product id which are no associated" +
                " When delete wish product Then throw not found exception")
        void postWishListNotFoundUserException()  {

            //When
            webTestClient.delete()
                    .uri("/wishlist/54/655")
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError -> {
                        //Then
                        assertThat(myResponseError.getMessage()).isEqualTo("The product with id 655 " +
                                                                                    "is not in your wishes");
                    });

        }
    }

    @Nested
    @DisplayName("When try to update fidelity points")
    class IncrementFidelityPoints {

        @Test
        @DisplayName("Given a exist id user then update the fidelity points")
        void patchFidelityPoints() {
            webTestClient.patch()
                    .uri("/fidelitypoints/1")
                    .bodyValue(50)
                    .exchange()
                    .expectStatus().isCreated()
                    .expectBody(UserDto.class)
                    .value(user ->
                        assertThat(user)
                                .hasFieldOrPropertyWithValue("name","Juan")
                                .hasFieldOrPropertyWithValue("email","juangarcia@example.com")
                                .hasFieldOrPropertyWithValue("fidelityPoints",150));
        }

        @Test
        @DisplayName("Given a non exist id user then throw not found error")
        void patchFidelityPointsUserError() {
            webTestClient.patch()
                    .uri("/fidelitypoints/9999")
                    .bodyValue(50)
                    .exchange()
                    .expectStatus().isNotFound()
                    .expectBody(MyResponseError.class)
                    .value(myResponseError ->
                        assertThat(myResponseError)
                                .hasFieldOrPropertyWithValue("code", HttpStatus.NOT_FOUND)
                                .hasFieldOrPropertyWithValue("message","The user with the id 9999 was not found."));
                    }
        }


    @Nested
    @DisplayName("Get a Country By Id ")
    class TestEnd2EndGetCountry {
    @Test
    @DisplayName("Given an  associated country id when call to get country endpoint Then return the correct country")
    void getCountryById() {
        //When
        webTestClient.get()
                .uri("/country/1")
                .exchange()
                .expectStatus().isOk()
                .expectBody(CountryDto.class)
                .value(countryDto -> {
                    //Then
                    assertThat(countryDto.getName()).isEqualTo("España");
                    assertThat(countryDto)
                            .hasFieldOrPropertyWithValue("id", 1L)
                            .hasFieldOrPropertyWithValue("tax", 21.0f)
                            .hasFieldOrPropertyWithValue("prefix", "+34")
                            .hasFieldOrPropertyWithValue("timeZone", "Europe/Madrid");
                });
    }
        @Test
        @DisplayName("Given a non associated country id when call to get country endpoint Then not found exceptions")
        void getCountryByNonAssociatedId() {

        //When
        webTestClient.get()
                .uri("/country/10")
                .exchange()
                .expectStatus().isNotFound()
                .expectBody(MyResponseError.class)
                .value(exception -> {
                    //Then
                    assertThat(exception).isEqualTo(MyResponseError.builder()
                            .code(HttpStatus.NOT_FOUND)
                            .message("Sorry! We're not in that country yet. We deliver to España, Estonia, Finlandia, Francia, Italia, Portugal, Grecia")
                            .build());
                });
        }
    }
}