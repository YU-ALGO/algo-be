package com.stock.yu.downbitbe.security.filter;

import com.stock.yu.downbitbe.domain.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
public class ApiLoginFilter extends AbstractAuthenticationProcessingFilter {
    private JWTUtil jwtUtil;

    public ApiLoginFilter(String defaultFilterProcessesUrl, JWTUtil jwtUtil) {
        super(defaultFilterProcessesUrl);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        log.info("----------ApiLoginFilter------------");
        log.info("attemptAuthentication");

        String userId = request.getParameter("username");
        String pw = request.getParameter("password");

        log.info("userId:"+userId);
        log.info("password:"+pw);
        log.info(request.getHeaderNames());
        log.info(request.getQueryString());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userId, pw);

        return getAuthenticationManager().authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        log.info("----------ApiLoginFilter------------");
        log.info("successfulAuthentication: " + authResult);

        log.info(authResult.getPrincipal());

        //email address
        String userId = ((UserAuthDTO) authResult.getPrincipal()).getUsername();

        String token = null;
        try {
            token = jwtUtil.generateToken(userId);

            //response.setContentType("text/plain");
            response.setContentType("application/json;charset=utf-8");
            response.getOutputStream().write(token.getBytes());

            log.info(token);
        } catch (Exception e) {
            e.printStackTrace();
        }

        log.info(authResult.getPrincipal());
    }
}
