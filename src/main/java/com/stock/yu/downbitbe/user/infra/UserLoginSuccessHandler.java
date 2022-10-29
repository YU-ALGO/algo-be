package com.stock.yu.downbitbe.user.infra;

import com.stock.yu.downbitbe.user.domain.user.UserAuthDTO;
import com.stock.yu.downbitbe.user.domain.user.LoginType;
import lombok.extern.log4j.Log4j2;
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
public class UserLoginSuccessHandler implements AuthenticationSuccessHandler {

    private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

    private PasswordEncoder passwordEncoder;

    public UserLoginSuccessHandler(PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("---------------------------");
        log.info("onAuthenticationSuccess ");

        UserAuthDTO userAuth = (UserAuthDTO) authentication.getPrincipal();
        boolean isSocial = !userAuth.getLoginType().equals(LoginType.LOCAL);
        log.info("Need Modify Member? " + isSocial);

        boolean isDefaultPassword = passwordEncoder.matches("1111", userAuth.getPassword());

        if(isSocial && isDefaultPassword) { // 소셜 로그인이면서 기본 비밀번호 사용시 연결되는 리다이렉트 링크임 -> 이름변경/비밀번호 변경 링크로 연결
            redirectStrategy.sendRedirect(request, response, "/member/modify?from=social"); //TODO : Need Fix ( from=social -> 아직 파악 못한 내용)
        }

    }
}
