package com.workshop.users.api.dto;

import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.io.Serializable;

@Data
@Builder
public class Login implements Serializable {
    @NonNull
    private String email;
    @NonNull
    private String password;

    public static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

    public boolean passwordMatch(String passwordHash){
        return BCRYPT.matches(getPassword(),passwordHash);
    }
}