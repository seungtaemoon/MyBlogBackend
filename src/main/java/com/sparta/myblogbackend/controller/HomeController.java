package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;

@RequiredArgsConstructor
@Controller
public class HomeController {
    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String home(Model model, @CookieValue(JwtUtil.AUTHORIZATION_HEADER) String username) {
        //jwtUtil.validateToken(jwtUtil.substringToken(username))//유효성 검사 , 재로그인 요청 이라든지
        model.addAttribute("username", jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(username)).getSubject());
        return "index";
    }
}