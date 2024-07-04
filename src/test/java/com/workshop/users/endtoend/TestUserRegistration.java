package com.workshop.users.endtoend;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.api.dto.Login;
import com.workshop.users.api.dto.UserDto;
import static org.assertj.core.api.Assertions.*;

import com.workshop.users.exceptions.MyResponseError;
import org.junit.jupiter.api.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClient;
@Disabled
class TestUserRegistration {
    private static RestClient restClient;

    @BeforeAll
    static void beforeAll() {
        restClient = RestClient.builder().baseUrl("https://user-microservice-ey3npq3qvq-uc.a.run.app/").build();
    }

    @Nested
    @DisplayName("Register")
    class Register {
        @Test
        @DisplayName("Given a valid user then creates a user")
        void testUserRegistration() {
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

            UserDto userDto = restClient
                    .post()
                    .uri("/register")
                    .body(newUser)
                    .retrieve()
                    .body(UserDto.class);

            assertThat(userDto).hasFieldOrPropertyWithValue("name", "Aria")
                    .hasFieldOrPropertyWithValue("email", newUser.getEmail())
                    .hasFieldOrPropertyWithValue("lastName", newUser.getLastName());
            assertThat(Login.BCRYPT.matches(newUser.getPassword(), userDto.getPassword())).isTrue();
            assertThat(userDto.getAddress().getStreet()).isEqualTo(newUser.getAddress().getStreet());
            assertThat(userDto.getCountry().getName()).isEqualTo(newUser.getCountry().getName());


        }

        @Test
        @DisplayName("Given a non valid user then throws an exception")
        void testUserRegistrationException() {
            UserDto newUser = UserDto.builder()
                    .name("Aria")
                    .lastName("Fei")
                    .email("aria@example.com")
                    .password("r1a31234.")
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
                            .name("Estonia")
                            .tax(21f)
                            .prefix("+34")
                            .timeZone("Europe/Madrid")
                            .build())
                    .build();

            assertThatThrownBy(() -> restClient
                    .post()
                    .uri("/register")
                    .body(newUser)
                    .retrieve()
                    .body(MyResponseError.class))
                    .isInstanceOf(HttpClientErrorException.class)
                    .extracting(Throwable::getMessage)
                    .isEqualTo( "400 Bad Request: \"{\"code\":\"BAD_REQUEST" +
                            "\",\"message\":\"The password must contain, at least, " +
                            "8 alphanumeric characters, uppercase, lowercase an special character.\"}\"");

        }



    }
    @Nested
    @DisplayName("Login")
    class LoginTest {
        @Test
        @DisplayName("Given a existing user that try to login then gets log")
        void testUserLogin() {
            Login login = Login.builder()
                    .email("juangarcia@example.com")
                    .password("1234")
                    .build();

            UserDto userDto = restClient
                    .post()
                    .uri("/login")
                    .body(login)
                    .retrieve()
                    .body(UserDto.class);

            assertThat(userDto).hasFieldOrPropertyWithValue("email", "juangarcia@example.com")
                    .hasFieldOrPropertyWithValue("name", "Juan")
                    .hasFieldOrPropertyWithValue("lastName", "Garca");
        }
    }

    @Test
    @DisplayName("Given a existing user that try to login then throw an exception")

    void testUserLoginException() {
        Login login = Login.builder()
                .email("juanagarcia@example.com")
                .password("1234")
                .build();

        assertThatThrownBy(() -> restClient
                .post()
                .uri("/login")
                .body(login)
                .retrieve()
                .body(MyResponseError.class))
                .isInstanceOf(HttpClientErrorException.class)
                .extracting(Throwable::getMessage)
                .isEqualTo( "404 Not Found: \"{\"code\":\"NOT_FOUND\",\"message\":" +
                        "\"The email or the password are incorrect.\"}\"");
    }
}
