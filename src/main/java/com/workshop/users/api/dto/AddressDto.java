package com.workshop.users.api.dto;

import com.workshop.users.model.AddressEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Data
@Builder
public class AddressDto implements Serializable {
    private Long id;
    private String cityName;
    private String zipCode;
    private String street;
    private Integer number;
    private String door;


    public static AddressEntity toEntity(AddressDto dto) {
        AddressEntity entity = new AddressEntity();
        entity.setId(dto.getId());
        entity.setCityName(dto.getCityName());
        entity.setZipCode(dto.getZipCode());
        entity.setStreet(dto.getStreet());
        entity.setNumber(dto.getNumber());
        entity.setDoor(dto.getDoor());
        return entity;
    }

}
