package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.security.utils.JWTUtil;
import com.stock.yu.downbitbe.user.domain.user.TokenRepository;
import com.stock.yu.downbitbe.user.domain.user.UserToken;
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
