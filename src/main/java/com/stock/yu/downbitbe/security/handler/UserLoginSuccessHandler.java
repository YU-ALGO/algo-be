package com.stock.yu.downbitbe.security.handler;

import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import com.stock.yu.downbitbe.user.dto.LoginCookiesDTO;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.Token;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

@Log4j2
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;

    public UserLoginSuccessHandler(PasswordEncoder passwordEncoder, JWTUtil jwtUtil) {
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------------------------");
        log.info("onAuthenticationSuccess ");

        UserAuthDTO userAuth = (UserAuthDTO) authentication.getPrincipal();
        boolean isSocial = !userAuth.getType().equals(LoginType.LOCAL);
        log.info("Need Modify Member? " + isSocial);
        boolean isDefaultPassword = passwordEncoder.matches("1111", userAuth.getPassword());

//        if(isSocial && isDefaultPassword) { // 소셜 로그인이면서 기본 비밀번호 사용시 연결되는 리다이렉트 링크임 -> 이름변경/비밀번호 변경 링크로 연결
//            redirectStrategy.sendRedirect(request, response, "/member/modify?from=social"); //TODO : Need Fix ( from=social -> 아직 파악 못한 내용)
//        }

        Token token = null;
        LoginCookiesDTO loginCookiesDTO = null;
        try {
            token = jwtUtil.generateToken(userAuth.getUserId(), userAuth.getNickname());
            loginCookiesDTO = jwtUtil.setLoginCookies(token, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, loginCookiesDTO.getIsLoginCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookiesDTO.getIsAdminCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookiesDTO.getAccessCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookiesDTO.getViewListCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookiesDTO.getRefreshCookie().toString());

        //TODO : 아래로 수정할 것 - 홈으로 갔을 때 특정 헤더를 붙이면 프론트에서
        response.sendRedirect(Config.WEB_BASE_URL);
        //redirectStrategy.sendRedirect(request, response, Config.WEB_BASE_URL);
    }
}
