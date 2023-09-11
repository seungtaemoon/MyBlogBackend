package com.sparta.myblogbackend.controller;

import com.sparta.myblogbackend.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
@Slf4j(topic = "Home Controller")
@RequiredArgsConstructor
@Controller
public class HomeController {

    private final JwtUtil jwtUtil;

    @GetMapping("/")
    public String home(Model model, @CookieValue("Authorization") String UserData) {

        try {
            log.info("id : " + jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(UserData)).getSubject());
            model.addAttribute("username", jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(UserData)).getSubject());
        }catch (Exception e)
        {
            log.warn("Convert Error : " + e.getMessage());
        }

        return "index";
    }

}