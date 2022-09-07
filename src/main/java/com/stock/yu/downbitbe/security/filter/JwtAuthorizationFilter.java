package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.user.service.CustomUserDetailsService;
import com.stock.yu.downbitbe.user.service.PrincipalDetails;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
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

        Enumeration<String> headers = request.getHeaderNames();
        while(headers.hasMoreElements()) {
            String s = headers.nextElement();
            String d = request.getHeader(s);
            log.info("header : " + s + "=" +d);
        }



        Cookie[] cookies = ((HttpServletRequest) request).getCookies();
        String token = null;
        if(cookies == null) {
            log.info("cookies is null");
            chain.doFilter(request, response);
            return;
        }

        Cookie cook = WebUtils.getCookie(request, "accessToken");
        log.info("coocccc : " + cook.getValue());

        if(cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("accessToken"))
                    log.info("cookie : " + cookie.getValue());
                    token = cookie.getValue().replace("Bearer ", "");
            }
        }
        log.info("token : " + token);

        /*String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("header : " + header);
        String token = request.getHeader("Authorization").replace("Bearer ", "");*/

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
