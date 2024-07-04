package com.workshop.users.api.controller.Data;

import com.workshop.users.api.dto.AddressDto;
import com.workshop.users.api.dto.CountryDto;
import com.workshop.users.api.dto.UserDto;
import lombok.Generated;

@Generated
public class DataInitzializerController {
    public final static AddressDto ADDRESS_VALLECAS;
    public final static CountryDto COUNTRY_SPAIN;
    public final static UserDto USER_LOGGED;
    public final static UserDto USER_REGISTERED;
    public final static UserDto USER_SAME_EMAIL;
    public final static AddressDto ADDRESS_VALLECAS_WITHOUT_ID;
    public final static UserDto USER_WITHOUT_ID;
    public final static UserDto INVALID_USER;

    static {
        ADDRESS_VALLECAS = AddressDto.builder()
                .id(3L)
                .cityName("Madrid")
                .street("C/VarajasNavaja")
                .zipCode("43567")
                .number(3)
                .door("1A")
                .build();
        ADDRESS_VALLECAS_WITHOUT_ID = AddressDto.builder()
                .cityName("Madrid")
                .street("C/VarajasNavaja")
                .zipCode("43567")
                .number(3)
                .door("1A")
                .build();

        COUNTRY_SPAIN = CountryDto.builder()
                .id(1L)
                .name("España")
                .tax(21F)
                .prefix("+34")
                .timeZone("Europe/Madrid")
                .build();

        USER_LOGGED = UserDto.builder()
                .id(2L)
                .email("manuel@example.com")
                .name("Manuel")
                .password("$2a$10$OyJUHBSm0sU8eF8os0ZuoOwDRmgg8ns4owWtIXItlYmN.1pDVxve6")
                .lastName("Salamanca")
                .phone("839234012")
                .birthDate("2000/12/01")
                .fidelityPoints(50)
                .country(COUNTRY_SPAIN)
                .address(ADDRESS_VALLECAS)
                .build();

        USER_WITHOUT_ID = UserDto.builder()
                .email("manuel@example.com")
                .name("Manuel")
                .password("12345678.")
                .lastName("Salamanca")
                .phone("839234012")
                .birthDate("2000/12/01")
                .fidelityPoints(50)
                .country(COUNTRY_SPAIN)
                .address(ADDRESS_VALLECAS_WITHOUT_ID)
                .build();

        USER_REGISTERED = UserDto.builder()
                .id(3L)
                .email("manuela@example.com")
                .name("Manuela")
                .password("12345678sXD@")
                .lastName("Salamanca")
                .phone("839234012")
                .birthDate("2000/12/01")
                .fidelityPoints(50)
                .country(COUNTRY_SPAIN)
                .address(ADDRESS_VALLECAS_WITHOUT_ID)
                .build();

        USER_SAME_EMAIL = UserDto.builder()
                .email("manuela@example.com")
                .name("Manuela")
                .password("12345678sXD@")
                .lastName("Salamanca")
                .phone("839234012")
                .birthDate("2000/12/01")
                .fidelityPoints(50)
                .country(COUNTRY_SPAIN)
                .address(ADDRESS_VALLECAS_WITHOUT_ID)
                .build();

        INVALID_USER = UserDto.builder()
                .id(3L)
                .email("manuelaexample.com")
                .name("Manuela")
                .password("12345678sXD")
                .lastName("Salamanca")
                .phone("83923401234243242342342")
                .birthDate("2020/2/01")
                .fidelityPoints(50)
                .country(COUNTRY_SPAIN)
                .address(ADDRESS_VALLECAS_WITHOUT_ID)
                .build();
    }
}
