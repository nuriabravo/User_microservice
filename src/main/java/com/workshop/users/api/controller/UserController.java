package com.workshop.users.api.controller;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.exceptions.CountryNotFoundException;
import com.workshop.users.exceptions.NotFoundAddressException;
import com.workshop.users.exceptions.NotFoundUserException;
import com.workshop.users.exceptions.UserValidationException;
import com.workshop.users.services.address.AddressService;
import com.workshop.users.services.country.CountryService;
import com.workshop.users.services.user.UserService;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
public class UserController {
    private final UserService userService;
    private final Validations validations;
    private final AddressService addressService;
    private final CountryService countryService;




    @GetMapping("/users/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable("id") Long id) throws NotFoundUserException {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable("id") Long id, @RequestBody UserDto updatedUserDto)
            throws UserValidationException, NotFoundUserException, NotFoundAddressException, CountryNotFoundException {
        updatedUserDto.setId(id);
        validations.checkAllMethods(updatedUserDto);
        AddressDto addressDtoUpdated = addressService.updateAddress(updatedUserDto.getAddress().getId(), updatedUserDto.getAddress());
        CountryDto countryDtoUpdated = countryService.getCountryByName(updatedUserDto.getCountry().getName());
        updatedUserDto.setAddress(addressDtoUpdated);
        updatedUserDto.setCountry(countryDtoUpdated);
        UserDto updatedUser = userService.updateUser(id, updatedUserDto);
        return new ResponseEntity<>(updatedUser, HttpStatus.CREATED);
    }

    @PatchMapping("/fidelitypoints/{id}")
    public ResponseEntity<UserDto> incrementFidelityPoints(@PathVariable Long id, @RequestBody Integer points) throws NotFoundUserException {
        return new ResponseEntity<>(userService.updateFidelityPoints(id, points), HttpStatus.CREATED);
    }
}