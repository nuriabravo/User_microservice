package com.workshop.users.model;

import com.workshop.users.api.dto.UserDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name="USERS")
@Data
public class UserEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="ID")
    private long id;
    @Column(name="NAME")
    @NotNull
    private String name;
    @Column(name="LAST_NAME")
    @NotNull
    private String lastName;
    @Column(name="EMAIL")
    @NotNull
    private String email;
    @Column(name="PASSWORD")
    @NotNull
    private String password;
    @ManyToOne
    @JoinColumn(name = "COUNTRY_ID", referencedColumnName = "ID")
    @NotNull
    private CountryEntity country;
    @Column(name= "FIDELITY_POINTS", columnDefinition = "integer default 0")
    private Integer fidelityPoints;
    @Column(name= "BIRTH_DATE")
    private LocalDate birthDate;
    @Column(name= "PHONE")
    @NotNull
    private String phone;
    @OneToOne
    @JoinColumn(name = "ADDRESS_ID", referencedColumnName = "ID")
    private AddressEntity address;


    public static UserDto fromEntity(UserEntity userEntity) {

        return UserDto.builder()
                .id(userEntity.getId())
                .name(userEntity.getName())
                .lastName(userEntity.getLastName())
                .email(userEntity.getEmail())
                .password(userEntity.getPassword())
                .fidelityPoints(userEntity.getFidelityPoints())
                .birthDate(parseDate(userEntity.getBirthDate()))
                .phone(userEntity.getPhone())
                .address(userEntity.getAddress()!=null?AddressEntity.fromEntity(userEntity.getAddress()):null)
                .country(CountryEntity.fromEntity(userEntity.getCountry()))
                .build();
    }

    public static String parseDate(LocalDate date){
        return date.toString().replace("-","/");
    }

}
