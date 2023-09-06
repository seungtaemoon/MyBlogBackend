package com.sparta.myblogbackend.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import com.sparta.myblogbackend.jwt.JwtUtil;

@Getter
@NoArgsConstructor
public class PostRequestDto {
    private String title;
    private String username;
    private String contents;
}
