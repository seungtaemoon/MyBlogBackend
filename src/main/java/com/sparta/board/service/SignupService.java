package com.sparta.board.service;

import com.sparta.board.dto.SignupRequestDto;

public interface SignupService {
    // 회원가입
    public Long signUp(SignupRequestDto requestDto) throws Exception;
}
