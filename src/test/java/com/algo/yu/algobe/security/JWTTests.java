package com.algo.yu.algobe.security;

import com.algo.yu.algobe.security.utils.JWTUtil;
import com.algo.yu.algobe.user.domain.user.Token;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class JWTTests {
    private JWTUtil jwtUtil;

    @BeforeEach
    public void testBefore() {
        System.out.println("testBefore............");
        jwtUtil = new JWTUtil();
    }

    @Test
    public void testEncode() throws Exception {
        String userId = "user95@test";
        String nickname = "tester";

        Token token = jwtUtil.generateToken(userId, nickname);
        String accessId = jwtUtil.validateAndExtract(token.getAccessToken());
        String refreshId = jwtUtil.validateAndExtract(token.getRefreshToken());

        if(!userId.equals(accessId)) throw new RuntimeException("엑세스 토큰 userId값 미일치");
        if(!userId.equals(refreshId)) throw new RuntimeException("리프레쉬 토큰 userID값 미일치");

        System.out.println(userId + " == " + accessId + " == " + refreshId);
    }

    @Test
    public void testValidate() throws Exception {
        String userId = "user95@test";
        String nickname = "tester";

        Token token = jwtUtil.generateToken(userId, nickname);

        Thread.sleep(5000);

        String accessId = jwtUtil.validateAndExtract(token.getAccessToken());
        String refreshId = jwtUtil.validateAndExtract(token.getRefreshToken());

        System.out.println(accessId + refreshId);
    }
}
