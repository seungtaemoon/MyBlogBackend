package com.sparta.myblogbackend.security;

import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.details.UserDetailsServiceImpl;
import com.sparta.myblogbackend.security.fillter.JwtAuthFilter;
import com.sparta.myblogbackend.security.fillter.LoginAuthFillter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig{

    // 로그인후 토큰을 받아 (UserName , Role) 게시물에 수정 권한 부여 / 로그인시에는 토큰 활용 X

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LoginAuthFillter loginAuthFillter() throws Exception
    {
        LoginAuthFillter fillter = new LoginAuthFillter(jwtUtil, "/api/loginProcess");
        //로그인시 ProcessUrl으로 데이터를 받아 처리함
        fillter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return  fillter;
    }

    @Bean
    public JwtAuthFilter jwtAuthFilter()
    {
        return new JwtAuthFilter(jwtUtil, userDetailsService);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        http.csrf(t -> t.disable());//CSRF 설정
        http.sessionManagement((sesstion) ->
                sesstion.sessionCreationPolicy(SessionCreationPolicy.STATELESS));//세션 비활성화

        http.authorizeHttpRequests(authHttpReq ->
                authHttpReq
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()//static 리소스 접근
                        .requestMatchers("/").permitAll()
                        .requestMatchers("/api/**").permitAll()
                        .anyRequest().authenticated()// 그외 인증 처리
                );

        http.formLogin(formLogin ->
                formLogin.loginPage("/api/user/auth/login")
                        .successHandler(
                                new AuthenticationSuccessHandler() {
                                    @Override
                                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
                                        response.sendRedirect("/index");
                                        System.out.println("-------------- Redirect ");
                                    }
                                }
                        )//Not Working This Part
                        //.failureUrl()
        );

        //필터 관리
        http.addFilterBefore(jwtAuthFilter(), LoginAuthFillter.class);
        http.addFilterBefore(loginAuthFillter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
