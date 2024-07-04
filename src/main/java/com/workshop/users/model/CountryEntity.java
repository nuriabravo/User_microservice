package com.workshop.users.model;

import com.workshop.users.api.dto.CountryDto;
import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;

@Entity
@Table(name="COUNTRIES")
@Data
public class CountryEntity implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private Long id;
    @Column(name="NAME")
    private String name;
    @Column(name="TAX")
    private Float tax;
    @Column(name="PREFIX")
    private String prefix;
    @Column(name="TIME_ZONE")
    private String timeZone;


    public static CountryDto fromEntity(CountryEntity entity){
        return CountryDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .tax(entity.getTax())
                .prefix(entity.getPrefix())
                .timeZone(entity.getTimeZone())
                .build();
    }
}
