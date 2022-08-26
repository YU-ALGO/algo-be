package com.stock.yu.downbitbe.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.stock.yu.downbitbe.domain.user.service.CustomUserDetailsService;
import com.stock.yu.downbitbe.domain.user.service.PrincipalDetails;
import com.stock.yu.downbitbe.security.payload.request.LoginRequest;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {

        log.info("----------JwtAuthenticationFilter------------");

        ObjectMapper om = new ObjectMapper();
        LoginRequest loginRequest = null;
        try {
            loginRequest = om.readValue(request.getInputStream(),LoginRequest.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info("JwtAuthenticationFilter : "+loginRequest);

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword());

        log.info("JwtAuthenticationFilter : 토큰생성완료");

        Authentication authentication = authenticationManager.authenticate(authenticationToken);

        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        log.info("Authentication : " + principalDetails.getUser().getUserId());

        return authentication;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("JwtAuthenticationFilter : successfulAuthentication");
        log.info(request.getParameter("username"));
        log.info(request.getParameter("password"));

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();
        try {
            String jwtToken = jwtUtil.generateToken(principalDetails.getUsername());

            response.addHeader("Authorization", "Bearer "+jwtToken);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
