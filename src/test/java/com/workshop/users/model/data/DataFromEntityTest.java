package com.workshop.users.model.data;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.api.dto.UserDto;
import com.workshop.users.model.AddressEntity;
import com.workshop.users.model.CountryEntity;
import com.workshop.users.model.UserEntity;
import lombok.Generated;

import java.util.Date;
@Generated
public class DataFromEntityTest {


    public final static AddressDto ADDRESS_DTO_CALLE_VARAJAS = AddressDto.builder()
            .id(3L)
            .cityName("Madrid")
            .street("C/VarajasNavaja")
            .zipCode("43567")
            .number(3)
            .door("1A")
            .build();

    public final static CountryDto COUNTRY_DTO_ESPANYA = CountryDto.builder()
            .id(1L)
            .name("España")
            .tax(21F)
            .prefix("+34")
            .timeZone("Europe/Madrid")
            .build();

    public final static UserDto USER_DTO_ID_2 =  UserDto.builder()
            .id(2L)
            .name("Denise")
            .lastName("Garrido")
            .email("denise@gmail.com")
            .address(ADDRESS_DTO_CALLE_VARAJAS)
            .birthDate("2004/04/14")
            .password("passwordAS123")
            .phone("12346789")
            .fidelityPoints(100)
            .country(COUNTRY_DTO_ESPANYA)
            .build();
    public final static AddressEntity ADDRESS_ENTITY_CALLE_VARAJAS = new AddressEntity();
    public final static CountryEntity COUNTRY_ENTITY_ESPANYA = new CountryEntity();
    public final static UserEntity  USER_ENTITY_ID_2 = new UserEntity();

    static{
        COUNTRY_ENTITY_ESPANYA.setId(1L);
        COUNTRY_ENTITY_ESPANYA.setName("España");
        COUNTRY_ENTITY_ESPANYA.setTax(21F);
        COUNTRY_ENTITY_ESPANYA.setPrefix("+34");
        COUNTRY_ENTITY_ESPANYA.setTimeZone("Europe/Madrid");

        ADDRESS_ENTITY_CALLE_VARAJAS.setId(3L);
        ADDRESS_ENTITY_CALLE_VARAJAS.setCityName("Madrid");
        ADDRESS_ENTITY_CALLE_VARAJAS.setStreet("C/VarajasNavaja");
        ADDRESS_ENTITY_CALLE_VARAJAS.setZipCode("43567");
        ADDRESS_ENTITY_CALLE_VARAJAS.setNumber(3);
        ADDRESS_ENTITY_CALLE_VARAJAS.setDoor("1A");

        USER_ENTITY_ID_2.setId(2L);
        USER_ENTITY_ID_2.setName("Denise");
        USER_ENTITY_ID_2.setLastName("Garrido");
        USER_ENTITY_ID_2.setEmail("denise@gmail.com");
        USER_ENTITY_ID_2.setAddress(ADDRESS_ENTITY_CALLE_VARAJAS);
        USER_ENTITY_ID_2.setBirthDate(UserDto.convertDateToLocalDate("2004/04/14"));
        USER_ENTITY_ID_2.setPassword("passwordAS123");
        USER_ENTITY_ID_2.setPhone("12346789");
        USER_ENTITY_ID_2.setFidelityPoints(100);
        USER_ENTITY_ID_2.setCountry(COUNTRY_ENTITY_ESPANYA);


    }

}
