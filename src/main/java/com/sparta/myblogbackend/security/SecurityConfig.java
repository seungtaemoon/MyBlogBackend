package com.sparta.myblogbackend.security;

import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.details.UserDetailsServiceImpl;
import com.sparta.myblogbackend.security.fillter.JwtAuthFilter;
import com.sparta.myblogbackend.security.fillter.LoginAuthFillter;
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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig {

    //그러니까 무한 리다이렉션 오류가 , 페이지 로드시 자동 로그인 시도 -> 새로고침 반복해서 그런건데...
    // **** 지금 상황이 회원가입시 쿠키 발행하고 , 로그인시 사용되는데 , 만약 쿠키를 사용자가 제거 하거나 만료되면 재발급은??

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
                formLogin.loginPage("/api/user/auth/login").permitAll()
                        //.defaultSuccessUrl()
                        //.failureUrl()
        );

        //필터 관리
        http.addFilterBefore(jwtAuthFilter(), LoginAuthFillter.class);
        http.addFilterBefore(loginAuthFillter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
