package com.workshop.users.api.controller;

import com.workshop.users.api.controller.Data.DataInitzializerController;
import com.workshop.users.api.controller.Data.DataToUserControllerTesting;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.UserValidationException;
import com.workshop.users.model.UserEntity;
import com.workshop.users.services.user.UserService;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;


import java.util.Optional;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ValidationTest {

    private UserService userService;

    private UserDto userDto;
    private Validations validations;
    private UserEntity userEntity;

    @BeforeEach
    void setUp() {
        userService = mock(UserService.class);
        validations = new Validations(userService);
        userDto = DataInitzializerController.USER_LOGGED;
        userEntity = UserDto.toEntity(userDto);
        userEntity.setId(2L);

    }

    @Nested
    @DisplayName("Checking the correct work of the valiedation methods")
    class CheckingFormat {
        @DisplayName("Given valid email format the correct format of the email")
        @Test
        void checkMailTest() throws UserValidationException {
            assertThat(validations.checkEmail(userDto))
                    .isTrue();
        }

        @DisplayName("Checking the correct format of the password")
        @Test
        void checkPasswordTest() throws UserValidationException {
            assertThat(validations.checkPassword(userDto))
                    .isTrue();
        }

        @DisplayName("Checking the correct format of the date")
        @Test
        void checkDateFormatTest() throws UserValidationException {
            assertThat(validations.checkDateFormat(userDto))
                    .isTrue();
        }

        @DisplayName("Checking that the user is of legal age")
        @Test
        void checkAgeTest() throws UserValidationException {
            assertThat(validations.checkAge(userDto))
                    .isTrue();
        }

        @DisplayName("Checking that the email not exists in any register of the database")
        @Test
        void checkSameEmailTest() throws UserValidationException {
            when(userService.getUserByEmailOptional(userDto.getEmail())).thenReturn(Optional.empty());
            boolean result = validations.checkSameEmail(userDto);
            assertThat(result).isFalse();
        }

        @DisplayName("Checking that the email exists in any register of the database")
        @Test
        void checkSameEmailWhenUserWantToChangeTheEmailTest() throws UserValidationException {
            when(userService.getUserByEmailOptional(userDto.getEmail())).thenReturn(Optional.of(userEntity));
            boolean result = validations.checkSameEmail(userDto);
            assertThat(result).isFalse();
        }

        @DisplayName("Checking that the email exists in any register of the database")
        @Test
        void checkSameEmailButTheEmailExistTest()  {
            UserDto userToChangeEmail = UserDto.builder().email("manolo@example.com").build();
            when(userService.getUserByEmailOptional(userToChangeEmail.getEmail())).thenReturn(Optional.of(userEntity));
            assertThatThrownBy(()-> validations.checkSameEmail(userToChangeEmail))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("Your email is already registered.");
        }


        @DisplayName("Checking the last five methods all-in-one.")
        @Test
        void checkAllMethodsUserTest() throws UserValidationException {
            when(userService.getUserByEmailOptional(userDto.getEmail())).thenReturn(Optional.empty());
            assertThat(validations.checkAllMethods(userDto))
                    .isTrue();

        }
    }

    @Nested
    @DisplayName("When the user try to change his/her data Then we check the email exists")
    class ExistsEmail {


        @Test
        @DisplayName("The email exists in the database, but it is himself.")
        void isExistsEmailAndNotIsFromTheUserFalseTest() {
            UserDto userToCheck = DataToUserControllerTesting.USER_ID_2;
            UserDto userDto = DataToUserControllerTesting.USER_ID_2;
            userDto.setEmail("newemail@gmail.com");

            boolean result = validations.isExistsEmailAndNotIsFromTheUser(userToCheck, userDto);
            assertThat(result).isFalse();
        }

        @Test
        @DisplayName("The email exists in the database and it is not from himself.")
        void isExistsEmailAndNotIsFromTheUserTest() {
            UserDto userToCheck = DataToUserControllerTesting.USER_ID_2;
            userDto = DataToUserControllerTesting.USER_ID_3;

            boolean resultOnceModified = validations.isExistsEmailAndNotIsFromTheUser(userToCheck, userDto);
            assertThat(resultOnceModified).isTrue();
        }
    }

    @Nested
    @DisplayName("Checking the incorrect work of the valiedation methods")
    class CheckingInvalidFormat {
        @Test
        @DisplayName("Checking the incorrect format of the email")
        void testEmailFormatInvalid(){
            UserDto userToCheck = DataInitzializerController.INVALID_USER;

            assertThatThrownBy(()->validations.checkEmail(userToCheck))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("Invalid email format.");

        }

        @Test
        @DisplayName("Checking the incorrect format of the password")
        void testPasswordFormatInvalid(){
            UserDto userToCheck = DataInitzializerController.INVALID_USER;
            assertThatThrownBy(()->validations.checkPassword(userToCheck))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("The password must contain, at least, 8 alphanumeric characters, uppercase, lowercase an special character.");
        }

        @Test
        @DisplayName("Checking the incorrect format of the birth date")
        void testBirthDateFormatInvalid(){
            UserDto userToCheck = DataInitzializerController.INVALID_USER;


            assertThatThrownBy(()->validations.checkDateFormat(userToCheck))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("The format of the birth date is not valid.");
        }

        @Test
        @DisplayName("Checking the incorrect format of the phone")
        void testPhoneFormatInvalid(){
            UserDto userToCheck = DataInitzializerController.INVALID_USER;



            assertThatThrownBy(()->validations.checkPhone(userToCheck))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("The phone must contain 9 numeric characters.");
        }
        @Test
        @DisplayName("Checking the user is under 18")
        void testIllegalAgeFormatInvalid(){
            UserDto userToCheck = DataInitzializerController.INVALID_USER;


            assertThatThrownBy(()->validations.checkAge(userToCheck))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("The user must be of legal age.");
        }
    }
    @Nested
    @DisplayName("Checking other user have the same email")
    class CheckingSomeUserHaveTheSameEmailTest {
        @Test
        @DisplayName("Checking other user have the same email")
        void ifSomeUserHaveSameEmailTest() throws UserValidationException {
            UserDto userDto = DataToUserControllerTesting.USER_ID_2;
            UserEntity userEntity = UserDto.toEntity(userDto);
            userEntity.setId(2L);
            validations.ifSomeUserHaveTheEmailThrowsError(userDto,userEntity);
        }

        @Test
        @DisplayName("Checking other user have the same email")
        void ifSomeUserHaveSameEmailThrowable()  {
            UserDto userDto = DataToUserControllerTesting.USER_ID_2;
            UserEntity userEntity = UserDto.toEntity(userDto);
            userEntity.setId(3L);
            assertThatThrownBy(()->validations.ifSomeUserHaveTheEmailThrowsError(userDto, userEntity))
                    .isInstanceOf(UserValidationException.class)
                    .hasMessage("Your email is already registered.");

        }

    }
}