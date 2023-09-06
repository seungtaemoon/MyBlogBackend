package com.sparta.myblogbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public class PostDeleteResponseDto {
    private int code;
    private HttpStatus httpStatus;
    private String message;

}
