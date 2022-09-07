package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.user.service.CustomUserDetailsService;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JwtTokenFilter extends OncePerRequestFilter {

    private JWTUtil jwtUtil;

    private CustomUserDetailsService userDetailsService;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

    }
}
