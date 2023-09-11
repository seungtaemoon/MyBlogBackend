package com.sparta.myblogbackend.security.handler;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.HandlesTypes;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

@Getter
@Slf4j(topic = "LoginSuccessHandler")
public class LoginSuccessHandler implements AuthenticationSuccessHandler {

    //이것도 작동 안되요.

    private String loginidname;
    private String defaultUrl;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        log.info("------------- 로그인 성공");
    }

    public void setLoginidname(String loginidname) {
        this.loginidname = loginidname;
    }

    public void setDefaultUrl(String defaultUrl) {
        this.defaultUrl = defaultUrl;
    }

}