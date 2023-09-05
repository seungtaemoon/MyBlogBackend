package com.sparta.myblogbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class LoginResponse {
    private int code;
    private HttpStatus httpStatus;
    private String message;
}
