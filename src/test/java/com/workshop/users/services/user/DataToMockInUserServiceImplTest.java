package com.workshop.users.services.user;


import com.workshop.users.api.dto.UserDto;
import com.workshop.users.model.AddressEntity;
import com.workshop.users.model.CountryEntity;
import com.workshop.users.model.UserEntity;
import lombok.Generated;

import java.util.Date;
@Generated

public class DataToMockInUserServiceImplTest {
    public final static AddressEntity ADDRESS_VALLECAS;
    public final static UserEntity USER_1_UPDATED;

    public final static CountryEntity COUNTRY_SPAIN;
    public final static UserEntity USER_1;

    static {
        ADDRESS_VALLECAS = new AddressEntity();
        ADDRESS_VALLECAS.setId(3L);
        ADDRESS_VALLECAS.setCityName("Madrid");
        ADDRESS_VALLECAS.setStreet("C/VarajasNavaja");
        ADDRESS_VALLECAS.setZipCode("43567");
        ADDRESS_VALLECAS.setNumber(3);
        ADDRESS_VALLECAS.setDoor("1A");

        COUNTRY_SPAIN = new CountryEntity();
        COUNTRY_SPAIN.setId(1L);
        COUNTRY_SPAIN.setName("España");
        COUNTRY_SPAIN.setTax(21F);
        COUNTRY_SPAIN.setPrefix("+34");
        COUNTRY_SPAIN.setTimeZone("Europe/Madrid");

        USER_1 = new UserEntity();
        USER_1.setId(2L);
        USER_1.setEmail("manuel@example.com");
        USER_1.setName("Manuel");
        USER_1.setPassword("2B8sda2?_");
        USER_1.setLastName("Salamanca");
        USER_1.setPhone("839234012");
        USER_1.setBirthDate(UserDto.convertDateToLocalDate("2000/01/14"));
        USER_1.setFidelityPoints(50);
        USER_1.setCountry(COUNTRY_SPAIN);
        USER_1.setAddress(ADDRESS_VALLECAS);

        USER_1_UPDATED = new UserEntity();
        USER_1_UPDATED.setId(2L);
        USER_1_UPDATED.setEmail("manuel@example.com");
        USER_1_UPDATED.setName("Manuel");
        USER_1_UPDATED.setPassword("2B8sda2?_");
        USER_1_UPDATED.setLastName("Salamanca");
        USER_1_UPDATED.setPhone("839234012");
        USER_1_UPDATED.setBirthDate(UserDto.convertDateToLocalDate("2000/01/14"));
        USER_1_UPDATED.setFidelityPoints(120);
        USER_1_UPDATED.setCountry(COUNTRY_SPAIN);
        USER_1_UPDATED.setAddress(ADDRESS_VALLECAS);


    }

}
