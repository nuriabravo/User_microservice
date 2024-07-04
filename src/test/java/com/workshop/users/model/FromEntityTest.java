package com.workshop.users.model;


import com.workshop.users.model.data.DataFromEntityTest;
import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class FromEntityTest {
    @Test
    @DisplayName("Given a UserEntity When fromEntity Then return a dto")
    void UserFromEntityTest() {
        UserEntity userEntity = DataFromEntityTest.USER_ENTITY_ID_2;
        assertThat(UserEntity.fromEntity(userEntity)).isEqualTo(DataFromEntityTest.USER_DTO_ID_2);
    }
    @Test
    @DisplayName("Given a CountryEntity When fromEntity Then return a dto")
    void CountryFromEntityTest() {
        CountryEntity countryEntity = DataFromEntityTest.COUNTRY_ENTITY_ESPANYA;
        assertThat(CountryEntity.fromEntity(countryEntity)).isEqualTo(DataFromEntityTest.COUNTRY_DTO_ESPANYA);
    }
    @Test
    @DisplayName("Given a AddressEntity When fromEntity Then return a dto")
    void AddressFromEntityTest() {
        AddressEntity addressEntity = DataFromEntityTest.ADDRESS_ENTITY_CALLE_VARAJAS;
        assertThat(AddressEntity.fromEntity(addressEntity)).isEqualTo(DataFromEntityTest.ADDRESS_DTO_CALLE_VARAJAS);
    }

    @Test
    @DisplayName("Given a UserEntity When fromEntity Then return a dto")
    void UserFromEntityTestWithNullAddress() {
        UserEntity userEntity = DataFromEntityTest.USER_ENTITY_ID_2;
        userEntity.setAddress(null);
        assertThat(UserEntity.fromEntity(userEntity)).isEqualTo(UserEntity.fromEntity(userEntity));
        userEntity.setAddress(DataFromEntityTest.ADDRESS_ENTITY_CALLE_VARAJAS);
    }

}
