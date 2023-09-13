package com.sparta.myblogbackend.security.fillter;

import com.sparta.myblogbackend.entity.UserRoleEnum;
import com.sparta.myblogbackend.jwt.JwtUtil;
import com.sparta.myblogbackend.security.details.UserDetailsImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.io.PrintWriter;

@Slf4j(topic = "로그인 및 JWT 생성")
public class LoginAuthFilter extends UsernamePasswordAuthenticationFilter {

    private final JwtUtil jwtUtil;
    private final String afterSuccessUrl;

    public LoginAuthFilter(JwtUtil jwtUtil, String processUrl, String afterSuccessUrl)
    {
        this.jwtUtil = jwtUtil;
        this.afterSuccessUrl = afterSuccessUrl;
        setFilterProcessesUrl(processUrl);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        {
            /*
            		if (this.postOnly && !request.getMethod().equals("POST")) {
			throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
		}
		String username = obtainUsername(request);
		username = (username != null) ? username.trim() : "";
		String password = obtainPassword(request);
		password = (password != null) ? password : "";
		UsernamePasswordAuthenticationToken authRequest = UsernamePasswordAuthenticationToken.unauthenticated(username,
				password);
		// Allow subclasses to set the "details" property
		setDetails(request, authRequest);
		return this.getAuthenticationManager().authenticate(authRequest);
            * */
        }//Disabled , Perent Code , 참고용 부모 코드


        UsernamePasswordAuthenticationToken authRequest
                = UsernamePasswordAuthenticationToken.unauthenticated(obtainUsername(request), obtainPassword(request));

        setDetails(request, authRequest);//서브클래스가 인증 요청의 세부 정보 속성에 입력되는 내용을 구성할 수 있도록 제공됩니다.

        if (!request.getMethod().equals("POST"))
            throw  new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());

        try
        {
            //LoginRequestDto requestDto = new ObjectMapper().readValue(request.getInputStream(), LoginRequestDto.class);
            //이게 문제..... 아에 변환을 못함 (예시로 있던거)
            //return  this.getAuthenticationManager().authenticate(authRequest);

            return getAuthenticationManager().authenticate(
              new UsernamePasswordAuthenticationToken(
                      obtainUsername(request),
                      obtainPassword(request),
                      null
              )
            );
        } catch (AuthenticationException e) {
            throw new RuntimeException("유효 하지 않는 데이터 : " + e.getMessage());
        }

    }


    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        log.info("로그인 성공");
        //UserDetailImpl 사용해 토큰 만들고 , 헤더 에 추가

        String username = ((UserDetailsImpl) authResult.getPrincipal()).getUsername();
        UserRoleEnum role = ((UserDetailsImpl) authResult.getPrincipal()).getUser().getRole();

        String token = jwtUtil.createToken(username, role);
        jwtUtil.addJwtToCookie(token, response);

        //AuthenticationSuccessHandler 구현 할 수 있지만 + 이 방식은 비동기 방식
        response.setStatus(HttpServletResponse.SC_OK);
        // 응답 본문에 메세지 작성
        PrintWriter writer = response.getWriter();
        writer.write("Login successful!");
        writer.flush();


        //response.sendRedirect(afterSuccessUrl);//성공시 리다이렉션 , 위의 상태코드 전송시 멈춰 오류남

    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {

        log.info("로그인 실패 : " + failed.getMessage());

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        // 응답 본문에 메세지 작성
        PrintWriter writer = response.getWriter();
        writer.write("Login fail!");
        writer.flush();

    }

}
