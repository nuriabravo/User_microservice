package com.workshop.users.model;

import com.workshop.users.api.dto.AddressDto;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name="ADDRESSES")
@Data
public class AddressEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name= "CITY_NAME")
    private String cityName;
    @Column(name= "ZIP_CODE")
    private String zipCode;
    @Column(name= "STREET")
    private String street;
    @Column(name= "NUMBER")
    private Integer number;
    @Column(name= "DOOR")
    private String door;

    public static AddressDto fromEntity(AddressEntity entity){
        return AddressDto.builder()
                .id(entity.getId())
                .cityName(entity.getCityName())
                .zipCode(entity.getZipCode())
                .street(entity.getStreet())
                .number(entity.getNumber())
                .door(entity.getDoor())
                .build();
    }


}
