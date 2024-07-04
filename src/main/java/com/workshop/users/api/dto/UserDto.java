package com.workshop.users.api.dto;

import com.workshop.users.model.UserEntity;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;

@Data
@Builder
public class UserDto implements Serializable {
    private Long id;
    @NotNull
    private String name;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String password;
    private Integer fidelityPoints;
    @NotNull
    private String birthDate;
    @NotNull
    private String phone;
    @NotNull
    private AddressDto address;
    @NotNull
    private CountryDto country;

    public static UserEntity toEntity(UserDto dto) {
        UserEntity entity = new UserEntity();
        entity.setName(dto.getName());
        entity.setLastName(dto.getLastName());
        entity.setEmail(dto.getEmail());
        entity.setPassword(dto.getPassword());
        entity.setFidelityPoints(dto.getFidelityPoints());
        entity.setBirthDate(convertDateToLocalDate(dto.getBirthDate()));
        entity.setPhone(dto.getPhone());
        entity.setAddress(dto.getAddress()!=null?AddressDto.toEntity(dto.getAddress()):null);
        entity.setCountry(CountryDto.toEntity(dto.getCountry()));

        return entity;
    }

    public boolean checkFormatEmail() {
        String emailFormat = "^[_A-Za-z0-9-+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        return getEmail().matches(emailFormat);
    }


    public boolean checkSecurityPassword() {
        String passwordSecureFormat =  "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@#$%^&+=!])(?=\\S+$).{8,}$";
        return getPassword().matches(passwordSecureFormat);
    }


    public boolean checkPhoneFormat() {
        String phoneFormat = "^\\d{9}$";
        return getPhone().matches(phoneFormat);
    }


    public boolean checkBirthDateFormat(){
        String dateFormat = "^\\d{4}/(0[1-9]|1[0-2])/(0[1-9]|[12]\\d|3[01])$";
        return getBirthDate().matches(dateFormat);
    }


    public boolean checkOver18() {
        String[] array = getBirthDate().split("/");
        LocalDate now = LocalDate.now();
        return  Period.between(LocalDate.of(Integer.parseInt(array[0]), Integer.parseInt(array[1]),Integer.parseInt(array[2])), now).getYears() >= 18;
    }


    public static Integer setSaveFidelityPoints(Integer userPoints, Integer addPoints) {
        int totalPoints = userPoints + addPoints;
        if (totalPoints < 0) {
            totalPoints = 0;
        }
        return  totalPoints;
    }

    public static LocalDate convertDateToLocalDate(String stringDate) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        return LocalDate.parse(stringDate, formatter);
    }

}