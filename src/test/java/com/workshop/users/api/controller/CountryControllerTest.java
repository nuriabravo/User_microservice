package com.workshop.users.api.controller;

import com.workshop.users.api.controller.Data.DataToUserControllerTesting;
import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.exceptions.CountryNotFoundException;
import com.workshop.users.services.country.CountryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

 class CountryControllerTest {

    private CountryService countryService;
    private CountryController countryController;

    @BeforeEach
    void setUp() {
        countryService = Mockito.mock(CountryService.class);
        countryController = new CountryController(countryService);
    }

    @DisplayName("Checking the correct functioning of get method")
    @Test
     void getCountry() throws CountryNotFoundException {
        CountryDto countryDto = DataToUserControllerTesting.COUNTRY_ESPANYA;
        when(countryService.getCountryById(1L)).thenReturn(countryDto);
        ResponseEntity<CountryDto> responseEntity = countryController.getCountry(1L);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(countryDto, responseEntity.getBody());
    }
}
