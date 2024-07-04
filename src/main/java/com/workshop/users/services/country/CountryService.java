package com.workshop.users.services.country;

import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.exceptions.CountryNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface CountryService {
    CountryDto getCountryById(Long id) throws CountryNotFoundException;
    CountryDto getCountryByName(String name) throws CountryNotFoundException;

}
