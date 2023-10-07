package com.sparta.myblogbackend.dto;

import lombok.Getter;
import lombok.Setter;

@Setter//은폐성 해제됨
@Getter
public class LoginRequestDto {
    private String username;
    private String password;
}