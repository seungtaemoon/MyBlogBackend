package com.sparta.myblogbackend.security;

import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.details.UserDetailsServiceImpl;
//import com.sparta.myblogbackend.security.fillter.JwtAuthFilter;
import com.sparta.myblogbackend.security.fillter.LoginAuthFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;

@Slf4j(topic = "SecurityConfig")
@Configuration
@EnableWebSecurity//spring Security 적용
@RequiredArgsConstructor
public class SecurityConfig{

    // 로그인후 토큰을 받아 (UserName , Role) 게시물에 수정 권한 부여 / 로그인시에는 토큰 활용 X
    // PostMan에서 테스트시 Body > x-www-form-urlencoded 에서 정보 입력후 전송
    //  formLogin.defaultSuccessUrl, successHandler, failureHandler 안됨

    private final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private final AuthenticationConfiguration authenticationConfiguration;


    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception
    {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public LoginAuthFilter loginAuthFillter() throws Exception
    {
        LoginAuthFilter filter = new LoginAuthFilter(jwtUtil, "/api/loginProcess", "/");
        //로그인시 ProcessUrl으로 데이터를 받아 처리함 / 성공시 리다이렉션
        //실패시는 따로 처리 안했지만 로그인 페이지 새로고침 됨
        filter.setAuthenticationManager(authenticationManager(authenticationConfiguration));
        return  filter;
    }

    /*
    @Bean
    public JwtAuthFilter jwtAuthFilter()
    {
        return new JwtAuthFilter(jwtUtil, userDetailsService);
    }*/
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception
    {
        try {
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
                                    .usernameParameter("username")
                                    .passwordParameter("password")
            );
            // defaultSuccessUrl, successHandler, failureHandler 안됨
                //--> LoginAuthFilter.(un)successfulAuthentication 에 하이제킹 당해 실행이 안됨, 뭔..
                // UserPassword인증필터 > 인증 메니저 > 인증Provider > UserDetailsService > AccountRepository
                // 참고 https://velog.io/@bey1548/Spring-Security-UsernamePasswordAuthenticationFilter
                // UserPassword인증필터 이후에 필터  동작 X

        }catch (Exception e)
        {
            log.warn("Redirect Error : " + e.getMessage());
        }


        //필터 관리
        //http.addFilterBefore(jwtAuthFilter(), LoginAuthFillter.class);
        http.addFilterBefore(loginAuthFillter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

}
