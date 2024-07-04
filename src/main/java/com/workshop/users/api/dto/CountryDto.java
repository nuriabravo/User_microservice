package com.workshop.users.api.dto;

import com.workshop.users.model.CountryEntity;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;


@Data
@Builder
public class CountryDto implements Serializable {
    private Long id;
    private String name;
    private Float tax;
    private String prefix;
    private String timeZone;

    public static CountryEntity toEntity(CountryDto dto) {
        CountryEntity entity = new CountryEntity();
        entity.setId(dto.getId());
        entity.setName(dto.getName());
        entity.setTax(dto.getTax());
        entity.setPrefix(dto.getPrefix());
        entity.setTimeZone(dto.getTimeZone());
        return entity;
    }

}