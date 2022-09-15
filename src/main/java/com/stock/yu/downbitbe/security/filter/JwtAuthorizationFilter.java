package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.domain.user.service.CustomUserDetailsService;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.Enumeration;

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
        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        if(cookies == null) {
            log.info("cookies is null");
            chain.doFilter(request, response);
            return;
        }

        String header2 = request.getHeader("Authorization");
        log.info("header2 : " + header2);
        Enumeration headers = request.getHeaderNames();
        while(headers.hasMoreElements()) {
            String val = (String) headers.nextElement();
            log.info("e : " + val + " = " + request.getHeader(val) );
        }
        String header = request.getHeader("authorization");
        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("header : " + header);
        String token = request.getHeader("authorization").replace("Bearer ", "");

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
