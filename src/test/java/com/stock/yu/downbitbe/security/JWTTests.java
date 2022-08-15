package com.stock.yu.downbitbe.security;

import com.stock.yu.downbitbe.security.utils.JWTUtil;
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

        String str = jwtUtil.generateToken(userId);
        System.out.println(str);
    }

    @Test
    public void testValidate() throws Exception {
        String userId = "user95@test";
        String str = jwtUtil.generateToken(userId);
        Thread.sleep(5000);
        String resultEmail = jwtUtil.validateAndExtract(str);

        System.out.println(resultEmail);
    }
}
