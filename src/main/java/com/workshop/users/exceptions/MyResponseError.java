package com.workshop.users.exceptions;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.io.Serializable;

@Data
@Builder
public class MyResponseError implements Serializable {
    private HttpStatus code;
    private String message;
}
