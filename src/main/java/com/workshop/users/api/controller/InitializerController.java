package com.workshop.users.api.controller;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.api.dto.Login;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.AuthenticateException;
import com.workshop.users.exceptions.RegisterException;
import com.workshop.users.exceptions.UserValidationException;
import com.workshop.users.services.address.AddressService;
import com.workshop.users.services.country.CountryService;
import com.workshop.users.services.user.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.transaction.annotation.Transactional;


@RestController
@AllArgsConstructor
public class InitializerController {
    private final UserService userService;
    private final AddressService addressService;
    private final CountryService countryService;
    private final Validations validations;


    @PostMapping("/register")
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<UserDto> addUser(@Validated @RequestBody UserDto user)
            throws RegisterException, UserValidationException {
        validations.checkAllMethods(user);

        AddressDto addressDto = user.getAddress();
        CountryDto countryDto = user.getCountry();
        AddressDto createdAddress = addressService.addAddress(addressDto);
        CountryDto createdCountry = countryService.getCountryByName(countryDto.getName());

        user.setAddress(createdAddress);
        user.setCountry(createdCountry);
        UserDto createdUser = userService.addUser(user);

        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> loginUser(@Validated @RequestBody Login userToLogIn) throws AuthenticateException {
        UserDto userToRespones = userService.getUserByEmail(userToLogIn.getEmail());
        if (userToLogIn.passwordMatch(userToRespones.getPassword())) {
            return new ResponseEntity<>(userToRespones, HttpStatus.OK);
        }
        throw new AuthenticateException("Can't authenticate");
    }
}