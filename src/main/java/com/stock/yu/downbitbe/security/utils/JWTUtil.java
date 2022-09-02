package com.stock.yu.downbitbe.security.utils;

import com.nimbusds.jose.mint.DefaultJWSMinter;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
    private String secretKey = "downbit"; // 실무에서는 유저의 비밀번호 등을 이용해서 넣어주는게 맞음

    private long expire = 50 * 24 * 30; //1달

    public String generateToken(String userId) throws Exception {
        return Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("userId", userId) //TODO sub -> userId
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
    }

    public String validateAndExtract(String tokenStr) throws Exception {
        String contentValue = null;

        try {
            DefaultJws defaultJws = (DefaultJws) Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(tokenStr);

            log.info(defaultJws);
            log.info(defaultJws.getBody().getClass());

            DefaultClaims claims = (DefaultClaims) defaultJws.getBody();

            log.info("-----------------");
            log.info("claims : " + claims);
            log.info("subject : " + claims.get("userId"));

            contentValue = (String) claims.get("userId");
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            contentValue = null;
        }

        return contentValue;
    }
}
