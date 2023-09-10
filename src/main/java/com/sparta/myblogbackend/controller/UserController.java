package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.dto.LoginRequestDto;
import com.sparta.myblogbackend.dto.LoginResponse;
import com.sparta.myblogbackend.dto.SignupRequestDto;
import com.sparta.myblogbackend.service.UserService;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Controller
@RequestMapping("/api")
public class UserController {

    private final UserService userService;


    @GetMapping("/user/login-page")
    public String loginPage() {
        return "login";
    }

    @GetMapping("/user/signup")
    public String signupPage() {
        return "signup";
    }

    @PostMapping("/user/signup")
    public ResponseEntity<LoginResponse> signup(@Valid SignupRequestDto requestDto, BindingResult bindingResult) {
        LoginResponse res = new LoginResponse(
                200,
                HttpStatus.OK,
                "회원 가입 성공!"
        );
        LoginResponse resfail = new LoginResponse(
                400,
                HttpStatus.BAD_REQUEST,
                "회원 가입 실패!"
        );
        // Validation 예외처리
        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        if(fieldErrors.size() > 0) {
            for (FieldError fieldError : bindingResult.getFieldErrors()) {
                log.error(fieldError.getField() + " 필드 : " + fieldError.getDefaultMessage());
            }
            return new ResponseEntity<>(resfail, resfail.getHttpStatus());
          //  return "redirect:/api/user/signup";
        }

        userService.signup(requestDto);
        return new ResponseEntity<>(res, res.getHttpStatus());
    }

    @PostMapping("/user/login")
    public ResponseEntity<LoginResponse> login(LoginRequestDto requestDto, HttpServletResponse res){
        LoginResponse response = new LoginResponse(
                200,
                HttpStatus.OK,
                "로그인 성공!!"
        );
        LoginResponse resfail = new LoginResponse(
                400,
                HttpStatus.BAD_REQUEST,
                "로그인 실패!"
        );
        try {
            userService.login(requestDto, res);
        } catch (Exception e) {
            return new ResponseEntity<>(resfail, resfail.getHttpStatus());
        }
        return new ResponseEntity<>(response, response.getHttpStatus());
    }

}