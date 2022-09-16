package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.user.service.CustomUserDetailsService;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.CookieValue;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    //private final CustomUserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JWTUtil jwtUtil) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        Cookie[] cookies = request.getCookies();

        if(cookies == null) {
            log.info("cookies is null");
            chain.doFilter(request, response);
            return;
        }

        Map<String, String> cookieMap = new HashMap<>();
        for (Cookie cookie : cookies) {
            cookieMap.put(cookie.getName(),cookie.getValue());
        }

        String token = cookieMap.getOrDefault("accessToken", null);
        if(token == null) {
            log.info("accessToken is null");
            chain.doFilter(request, response);
            return;
        }

        try {
            String userId = jwtUtil.validateAndExtract(token);

            log.info("user_id : "+userId);
            if (userId != null) {
                UserDetails user = userDetailsService.loadUserByUsername(userId);
                //PrincipalDetails userDetails = new PrincipalDetails(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
