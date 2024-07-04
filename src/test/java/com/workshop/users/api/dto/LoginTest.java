package com.workshop.users.api.dto;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class LoginTest {

    private Login login;

    @BeforeEach
    void setUp() {
        login = Login.builder().email("manolito").password("12345678").build();
    }

    @Nested
    @DisplayName("When password match")
    class PasswordMatch{
        @Test
        @DisplayName("Given a good password Then return true")
        void passwordMatch() {
            //Given
            String password = Login.BCRYPT.encode("12345678");
            //When and Then
            assertThat(login.passwordMatch(password)).isTrue();
        }
        @Test
        @DisplayName("Given bad password Then return false")
        void passwordDoesntMatch() {
            //Given
            String password = Login.BCRYPT.encode("abdcefghy");
            //When and Then
            assertThat(login.passwordMatch(password)).isFalse();
        }

    }

}