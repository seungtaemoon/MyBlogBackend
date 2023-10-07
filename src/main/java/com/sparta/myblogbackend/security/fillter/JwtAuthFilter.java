package com.sparta.myblogbackend.security.fillter;

import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.details.UserDetailsServiceImpl;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.net.URLDecoder;

//테스트 결과 굳이 재정의 할 필요 없을꺼 같아요


@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthFilter extends OncePerRequestFilter {// (Replace AuthenticationFilter)

    // 토큰이 유효하지 않을때 로그인 으로 넘기기 - 아마 그렇게 될꺼 같기도 함
    // @AuthenticationPrincipal 사용시 이 필터에 지나가면서 유효한 사용자인지 검사

    private  final JwtUtil jwtUtil;
    private final UserDetailsServiceImpl userDetailsService;
    private String[] passUrls;

    public JwtAuthFilter(JwtUtil jwtUtil, UserDetailsServiceImpl userDetailsService, String ... passUrl) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.passUrls = passUrl;
    }

    private boolean passAuthentication(HttpServletRequest request , String passPath) {
        String path = request.getRequestURI();
        return path.startsWith(passPath);
    }//메소드도 구분 필요

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException
    {
        log.info("shouldNotFilter");
        if (PathRequest.toStaticResources().atCommonLocations().matches(request))
            return true;

        for (String p : passUrls)
        {
            if (passAuthentication(request, p))
                return true;
        }//커스텀으로 하면 검증 통과할 URL 직접 구현 Tlqkf

        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        if (passAuthentication(request, "/api/user/login"))//커스텀으로 하면 검증 통과할 URL 직접 구현 Tlqkf
        {
            filterChain.doFilter(request, response);
            log.info("Pass Auth - LoginPage ");
            return;
        }else
            log.info("Not Pass Auth - LoginPage : " + request.getRequestURI());

        //log.info("process Get Cookie : " + request.getHeader(JwtUtil.AUTHORIZATION_HEADER));//이걸로 못찾음 (http 요청임)

        String cookieValue = null;
        for (Cookie cookie : request.getCookies())
        {
            if (JwtUtil.AUTHORIZATION_HEADER.equals(cookie.getName()))
            {
                cookieValue = cookie.getValue();
                break;
            }
        }

        String tokenValue = jwtUtil.substringToken(URLDecoder.decode(cookieValue, "UTF-8"));

        if (StringUtils.hasText(tokenValue))
        {
            if (!jwtUtil.validateToken(tokenValue))
                throw new ServletException("유효 하지 않은 토큰");

            Claims info = jwtUtil.getUserInfoFromToken(tokenValue);

            try {
                setAuthentication(info.getSubject());
            } catch (Exception e)
            {
                log.error(e.getMessage());
            }

            log.info("토큰 검증 완료 : " + response.getStatus());
        }else
            log.warn("Token is Empty");

        
        filterChain.doFilter(request, response);
    }

    // 인증 처리
    public void setAuthentication(String username) {
        log.info("setAuthentication");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }//신임장 -> null
}
