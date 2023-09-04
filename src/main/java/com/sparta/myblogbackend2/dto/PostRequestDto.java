package com.sparta.myblogbackend2.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String username;
    private String contents;
    private String password;
}
