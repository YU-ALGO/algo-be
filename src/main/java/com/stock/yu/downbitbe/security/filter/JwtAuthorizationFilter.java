package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.user.application.CustomUserDetailsService;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import com.stock.yu.downbitbe.user.application.TokenService;
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
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    //private final CustomUserRepository userRepository;
    private final CustomUserDetailsService userDetailsService;
    private final JWTUtil jwtUtil;
    private final TokenService tokenService;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserDetailsService userDetailsService, JWTUtil jwtUtil, TokenService tokenService) {
        super(authenticationManager);
        this.userDetailsService = userDetailsService;
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
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

            String refresh = cookieMap.getOrDefault("refreshToken", null);
            if(refresh != null) {
                String accessToken;
                try {
                    accessToken = jwtUtil.regenerateToken(refresh);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                Cookie accessCookie = new Cookie("accessToken", accessToken);
                accessCookie.setPath("/");
                accessCookie.setHttpOnly(true);
                accessCookie.setMaxAge((int) JWTUtil.accessExpire);
                accessCookie.setDomain(Config.DOMAIN);
                response.addCookie(accessCookie);

                Cookie viewListCookie = new Cookie("viewList", "");
                viewListCookie.setPath("/");
                viewListCookie.setHttpOnly(true);
                viewListCookie.setMaxAge((int) JWTUtil.accessExpire);
                viewListCookie.setDomain(Config.DOMAIN);
                response.addCookie(viewListCookie);

                Cookie foodListCookie = new Cookie("foodList", "");
                foodListCookie.setPath("/");
                foodListCookie.setHttpOnly(true);
                foodListCookie.setMaxAge((int) JWTUtil.accessExpire);
                foodListCookie.setDomain(Config.DOMAIN);
                response.addCookie(foodListCookie);

            }

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
