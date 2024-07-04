package com.workshop.users.api.controller;

import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.UserValidationException;
import com.workshop.users.model.UserEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import com.workshop.users.services.user.UserService;

import java.util.Optional;

@Controller
@AllArgsConstructor
public class Validations {
    private final UserService userService;


    boolean checkEmail(UserDto userToCheck) throws UserValidationException {
        if (!userToCheck.checkFormatEmail()) {
            throw new UserValidationException("Invalid email format.");
        }
        return true;
    }

    boolean checkPassword(UserDto userToCheck) throws UserValidationException {
        if (!userToCheck.checkSecurityPassword()) {
            throw new UserValidationException("The password must contain, at least," +
                                        " 8 alphanumeric characters, uppercase, " +
                                        "lowercase an special character.");
        }
        return true;
    }

    boolean checkDateFormat(UserDto userToCheck) throws UserValidationException {
        if (!userToCheck.checkBirthDateFormat()) {
            throw new UserValidationException("The format of the birth date is not valid.");
        }
        return true;
    }

    boolean checkPhone(UserDto userToCheck) throws UserValidationException {
        if (!userToCheck.checkPhoneFormat()) {
            throw new UserValidationException("The phone must contain 9 numeric characters.");
        }
        return true;
    }

    boolean checkAge(UserDto userToCheck) throws UserValidationException {
        if (!userToCheck.checkOver18()) {
            throw new UserValidationException("The user must be of legal age.");
        }
        return true;
    }

    boolean checkSameEmail(UserDto userToCheck) throws UserValidationException {
        String email = userToCheck.getEmail();
        Optional<UserEntity> userOptional = userService.getUserByEmailOptional(email);
        if (userOptional.isPresent()) {
            ifSomeUserHaveTheEmailThrowsError(userToCheck, userOptional.orElseThrow());
        }
        return false;
    }

    void ifSomeUserHaveTheEmailThrowsError(UserDto userToCheck, UserEntity userWithTheSameEmail)
            throws UserValidationException {
        UserDto userDto = UserEntity.fromEntity(userWithTheSameEmail);
        if (isExistsEmailAndNotIsFromTheUser(userToCheck, userDto)) {
            throw new UserValidationException("Your email is already registered.");
        }
    }


    boolean isExistsEmailAndNotIsFromTheUser(UserDto userToCheck, UserDto userDto) {
        return userToCheck.getId() == null || !userDto.getId().equals(userToCheck.getId());
    }

    boolean checkAllMethods(UserDto userToCheck) throws UserValidationException {
        checkEmail(userToCheck);
        checkPassword(userToCheck);
        checkDateFormat(userToCheck);
        checkPhone(userToCheck);
        checkAge(userToCheck);
        checkSameEmail(userToCheck);
        return true;
    }
}