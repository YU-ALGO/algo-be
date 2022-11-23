package com.algo.yu.algobe.user.application;

import com.algo.yu.algobe.security.utils.JWTUtil;
import com.algo.yu.algobe.user.domain.user.TokenRepository;
import com.algo.yu.algobe.user.domain.user.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class TokenService {
    private final TokenRepository tokenRepository;

    public String insertRefreshToken(String username, String refreshToken) {
        return tokenRepository.save(UserToken.builder()
                .username(username)
                .refreshToken(refreshToken)
                .validTime(LocalDateTime.now().plusSeconds(JWTUtil.refreshExpire))
                .build()).getUsername();
    }

    public Boolean validateRefreshToken(String username, String code) {
        UserToken userToken = tokenRepository.findById(username).get();
        if(!userToken.getRefreshToken().equals(code))
            return false;
        return !userToken.getValidTime().isBefore(LocalDateTime.now());
    }
}
