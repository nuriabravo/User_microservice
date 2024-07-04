package com.workshop.users.model;


import static org.junit.jupiter.api.Assertions.*;

import com.workshop.users.api.dto.UserDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;


class UserTest {
    @Nested
    @DisplayName("WhenCheckEmail")
    class TestComprobeEmail {
        @Test
        @DisplayName("GivenValidEmail_WhenValidEmail_ThenReturnTrue")
        void testCheckFormatEmail_ValidEmail() {
            // Given
            UserDto userDto = UserDto.builder()
                    .email("test@example.com")
                    .build();

            // When
            boolean result = userDto.checkFormatEmail();

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("GivenInvalidEmail_WhenValidEmail_ThenReturnFalse")
        void testCheckFormatEmail_InvalidEmail() {
            // Given
            UserDto userDto = UserDto.builder()
                    .email("invalid_email")
                    .build();

            // When
            boolean result = userDto.checkFormatEmail();

            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("WhenCheckPassword")
    class TestComprobePassword {
        @Test
        @DisplayName("GivenValidPassword_WhenCheckPassword_ThenReturnTrue")
        void testCheckSecurityPassword_ValidPassword() {
            // Given
            UserDto userDto = UserDto.builder()
                    .password("SecurePassword1!")
                    .build();
            // When
            boolean result = userDto.checkSecurityPassword();
            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("GivenInalidPassword_WhenCheckPassword_ThenReturnFalse")
        void testCheckSecurityPassword_InvalidPassword() {
            // Given
            UserDto userDto = UserDto.builder()
                    .password("weakpassword")
                    .build();
            // When
            boolean result = userDto.checkSecurityPassword();
            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("WhenCheckPhone")
    class TestComprobePhone {
        @Test
        @DisplayName("GivenValidPhone_WhenCheckPhone_ThenReturnTrue")
        void testCheckPhoneFormat_ValidPhone() {
            // Given
            UserDto userDto = UserDto.builder()
                    .phone("123456789")
                    .build();
            // When
            boolean result = userDto.checkPhoneFormat();
            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("GivenInvalidPhone_WhenCheckPhone_ThenReturnFalse")
        void testCheckPhoneFormat_InvalidPhone() {
            // Given
            UserDto userDto = UserDto.builder()
                    .phone("1234567")
                    .build();
            // When
            boolean result = userDto.checkPhoneFormat();
            // Then
            assertFalse(result);
        }
    }

    @Nested
    @DisplayName("WhenCheckBirthDate")
    class TestComprobeBirthDate {
        @Test
        @DisplayName("GivenValidBirthDate_WhenCheckBirthDate_ThenReturnTrue")
        void testCheckBirthDateFormat_ValidDate() {
            // Given
            UserDto userDto = UserDto.builder()
                    .birthDate("2000/04/12")
                    .build();
            // When
            boolean result = userDto.checkBirthDateFormat();

            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("GivenInvalidBirthDate_WhenCheckBirthDate_ThenReturnFalse")
        void testCheckBirthDateFormat_InvalidDate() {
            // Given
            UserDto userDto = UserDto.builder()
                    .birthDate("12/04/2000")
                    .build();
            // When
            boolean result = userDto.checkBirthDateFormat();

            // Then
            assertFalse(result);
        }

        @Test
        @DisplayName("GivenOlder18Date_WhenCheckOlder18Date_ThenReturnTrue")
        void testIsOver18_Over18() {
            // Given
            UserDto userDto = UserDto.builder()
                    .birthDate("2000/04/12")
                    .build();
            // When
            boolean result = userDto.checkOver18();
            // Then
            assertTrue(result);
        }

        @Test
        @DisplayName("GivenNotOlder18Date_WhenCheckOlder18Date_ThenReturnFalse")
        void testIsOver18_Under18() {
            // Given
            UserDto userDto = UserDto.builder()
                    .birthDate("2023/04/12")
                    .build();
            // When
            boolean result = userDto.checkOver18();
            // Then
            assertFalse(result);
        }
    }




    @Nested
    @DisplayName("Set save fidelity points")
    class FidelityPoints {
        @Test
        @DisplayName("Given a valid fidelity points then update them")
        void setSaveFidelityPointsTest() {
            Integer result = UserDto.setSaveFidelityPoints(2, 2);
            assertThat(result).isNotNull().isEqualTo(4);
        }

        @Test
        @DisplayName("Given a non valid fidelity points then return0")
        void setSaveFidelityPointsTestReturnZero() {
            Integer result = UserDto.setSaveFidelityPoints(2, -50);
            assertThat(result).isNotNull().isZero();
        }
    }

    @Nested
    @DisplayName("Set local date")
    class ToLocalDateTest {
        @Test
        @DisplayName("Given a valid date then return a LocalDate")
        void convertDateToLocalDateTest() {
            String stringDate = "1980/10/14";
            LocalDate localDate = UserDto.convertDateToLocalDate(stringDate);
            assertThat(localDate).isInstanceOf(LocalDate.class);
        }
    }
}

