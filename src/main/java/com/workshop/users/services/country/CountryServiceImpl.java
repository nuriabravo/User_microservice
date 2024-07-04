package com.workshop.users.services.country;

import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.exceptions.CountryNotFoundException;
import com.workshop.users.model.CountryEntity;
import com.workshop.users.repositories.CountryDAORepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CountryServiceImpl implements CountryService {

    private final CountryDAORepository countryDAORepository;
    private static final String COUNTRY_MESSAGE = "Sorry! We're not in that country yet. We deliver " +
            "to España, Estonia, Finlandia, Francia, Italia, Portugal, Grecia";


    @Override
    public CountryDto getCountryById(Long id) throws CountryNotFoundException{
        isNotNull(id);
        CountryEntity countryEntity = countryDAORepository.findById(id)
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_MESSAGE));
        return CountryEntity.fromEntity(countryEntity);
    }

    @Override
    public CountryDto getCountryByName(String name) throws CountryNotFoundException {
        isNotNull(name);
        CountryEntity countryEntity = countryDAORepository.findByName(name)
                .orElseThrow(() -> new CountryNotFoundException(COUNTRY_MESSAGE));
        return CountryEntity.fromEntity(countryEntity);
    }

    private void isNotNull(Object obj) throws CountryNotFoundException  {
        if (obj == null) {
            throw new CountryNotFoundException(COUNTRY_MESSAGE);
        }
    }
}
