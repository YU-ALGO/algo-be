package com.algo.yu.algobe.security.handler;

import com.algo.yu.algobe.security.utils.JWTUtil;
import com.algo.yu.algobe.security.config.Config;
import com.algo.yu.algobe.user.application.UserAllergyInfoService;
import com.algo.yu.algobe.user.domain.user.LoginCookies;
import com.algo.yu.algobe.user.domain.user.LoginType;
import com.algo.yu.algobe.user.domain.user.Token;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Log4j2
@RequiredArgsConstructor
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private final PasswordEncoder passwordEncoder;
    private final JWTUtil jwtUtil;
    private final UserAllergyInfoService allergyInfoService;


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------------------------");
        log.info("onAuthenticationSuccess ");

        UserAuthDto userAuth = (UserAuthDto) authentication.getPrincipal();
        boolean isSocial = !userAuth.getLoginType().equals(LoginType.LOCAL);
        log.info("Need Modify Member? " + isSocial);
        boolean isDefaultPassword = passwordEncoder.matches("1111", userAuth.getPassword());

        if(isSocial) { // 소셜 로그인이면서 기본 비밀번호 사용시 연결되는 리다이렉트 링크임 -> 이름변경/비밀번호 변경 링크로 연결
            if(!allergyInfoService.existsUserByUserId(userAuth.getUsername())) {
                allergyInfoService.saveAllergyInfo(userAuth.getUsername());
            }
        }

        Token token = null;
        LoginCookies loginCookies = null;
        try {
            token = jwtUtil.generateToken(userAuth.getUsername(), userAuth.getNickname());
            loginCookies = jwtUtil.setLoginCookies(token, false);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        response.addHeader(HttpHeaders.SET_COOKIE, loginCookies.getIsLoginCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookies.getIsAdminCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookies.getAccessCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookies.getViewListCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookies.getFoodListCookie().toString());
        response.addHeader(HttpHeaders.SET_COOKIE, loginCookies.getRefreshCookie().toString());

        //TODO : 아래로 수정할 것 - 홈으로 갔을 때 특정 헤더를 붙이면 프론트에서
        response.sendRedirect(Config.WEB_BASE_URL);
        //redirectStrategy.sendRedirect(request, response, Config.WEB_BASE_URL);
    }
}
