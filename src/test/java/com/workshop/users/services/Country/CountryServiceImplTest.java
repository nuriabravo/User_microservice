package com.workshop.users.services.Country;

import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.exceptions.CountryNotFoundException;
import com.workshop.users.model.CountryEntity;
import com.workshop.users.repositories.CountryDAORepository;
import com.workshop.users.services.country.CountryService;
import com.workshop.users.services.country.CountryServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CountryServiceImplTest {
    private CountryDAORepository countryDAORepository;
    private CountryService countryService;
    private CountryDto countryDto;
    private CountryEntity countryEntity;

    @BeforeEach
    void setUp() {
        countryDAORepository = mock(CountryDAORepository.class);
        countryService = new CountryServiceImpl(countryDAORepository);
        countryDto = CountryDto.builder()
                .id(1L)
                .tax(21F)
                .name("España")
                .prefix("+34")
                .timeZone("Europe/Madrid")
                .build();

        countryEntity = new CountryEntity();
        countryEntity.setId(1L);
        countryEntity.setTax(21F);
        countryEntity.setName("España");
        countryEntity.setPrefix("+34");
        countryEntity.setTimeZone("Europe/Madrid");
    }

    @Test
    @DisplayName("When try to get a Country by Id then return the correct country")
    void getCountryById() {
        when(countryDAORepository.findById(countryDto.getId())).thenReturn(Optional.of(countryEntity));
        CountryDto foundCountry = countryService.getCountryById(countryEntity.getId());
        assertThat(foundCountry).isEqualTo(CountryEntity.fromEntity(countryEntity));
    }

    @Test
    @DisplayName("When try to get a Country by name then return the correct country")
    void getCountryByName() {
        when(countryDAORepository.findByName(countryDto.getName())).thenReturn(Optional.of(countryEntity));
        CountryDto foundCountry = countryService.getCountryByName(countryEntity.getName());
        assertThat(foundCountry).isEqualTo(CountryEntity.fromEntity(countryEntity));
    }

    @Test
    @DisplayName("When try to get a Country by an incorrect name then throw an error")
    void getCountryByIncorrectName() {
        assertThatThrownBy(() -> countryService.getCountryByName("Gftlandia"))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessage("Sorry! We're not in that country yet. We deliver to España, Estonia, Finlandia, Francia, Italia, Portugal, Grecia");
    }

    @Test
    @DisplayName("Given a null Id then throw an error")
    void getCountryErrorIdNull() {
        assertThatThrownBy(() -> countryService.getCountryById(null))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessage("Sorry! We're not in that country yet. We deliver to España, Estonia, Finlandia, Francia, Italia, Portugal, Grecia");
    }

    @Test
    @DisplayName("Given a null name then throw an error")
    void getCountryErrorNameNull() {
        assertThatThrownBy(() -> countryService.getCountryByName(null))
                .isInstanceOf(CountryNotFoundException.class)
                .hasMessage("Sorry! We're not in that country yet. We deliver to España, Estonia, Finlandia, Francia, Italia, Portugal, Grecia");
    }
}
