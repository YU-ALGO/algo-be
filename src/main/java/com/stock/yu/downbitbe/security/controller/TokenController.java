package com.stock.yu.downbitbe.security.controller;

import com.stock.yu.downbitbe.user.entity.Token;
import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/token")
public class TokenController {

    private final JWTUtil jwtUtil;

    @GetMapping(value = "/validate")
    public ResponseEntity<?> validateToken(@CookieValue("accessToken") String accessToken) throws Exception {
        if(!jwtUtil.isValidToken(accessToken))
            return ResponseEntity.badRequest().body("잘못된 토큰입니다");

        return ResponseEntity.ok().body(true);
    }

    @PostMapping(value = "/refresh")
    public ResponseEntity<?> validateRefreshToken(@CookieValue("refreshToken") String refreshToken) throws Exception {

        if(!jwtUtil.isValidToken(refreshToken))
            return ResponseEntity.badRequest().build();

        String newToken = jwtUtil.regenerateToken(refreshToken);



        return ResponseEntity.ok().build();
    }


}
