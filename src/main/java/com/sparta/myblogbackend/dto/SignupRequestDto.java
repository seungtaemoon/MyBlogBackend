package com.sparta.myblogbackend.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter//<------- 은폐성 해제됨
public class SignupRequestDto {
    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z]).{4,10}",message = "최소 4자 이상, 10자 이하이며 알파벳 소문자(a~z), 숫자(0~9)로 구성되어야 합니다!")
    private String username;

    @Pattern(regexp = "(?=.*[0-9])(?=.*[a-z])(?=.*[^0-9a-zA-Z]).{8,15}",message = "최소 8자 이상, 15자 이하이며 알파벳 대소문자(a~z, A~Z), 숫자(0~9) 및 특수문자로 구성되어야 합니다!")
    private String password;//모든 특수 문자 허용시 , 특정 로직 실행 가능!

    private String email;
    private boolean admin = false;
    private String adminToken = "";
}