package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.domain.user.entity.User;
import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.domain.user.service.PrincipalDetails;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {
    private final CustomUserRepository userRepository;
    private JWTUtil jwtUtil;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, CustomUserRepository userRepository) {
        super(authenticationManager);
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer")) {
            chain.doFilter(request, response);
            return;
        }
        log.info("header" + header);
        String token = request.getHeader("'Authorization").replace("Bearer ", "");

        try {
            String userId = jwtUtil.validateAndExtract(token);

            if (userId != null) {
                User user = userRepository.findByUserId(userId);

                PrincipalDetails principalDetails = new PrincipalDetails(user);

                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            chain.doFilter(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
