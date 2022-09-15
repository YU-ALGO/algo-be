package com.stock.yu.downbitbe.security.utils;

import com.stock.yu.downbitbe.domain.user.entity.Token;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JWTUtil {
    private String secretKey = "downbit"; // 실무에서는 유저의 비밀번호 등을 이용해서 넣어주는게 맞음

    public static long accessExpire = 60 * 60 * 24; //2시간 / 단위 : 초
    public static long refreshExpire = 60 * 10; //10분

    /*
    *
    * refreshToken
    * 유효기간 : 24시간
    * subject : userId
    * claims : [nickname]
    *
    * accessToken
    * 유효기간 : 10분
    * subject : userId
    * claims : [null]
    *
    *
    *
    * */

    public Token generateToken(String userId, String nickname) throws Exception {
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(refreshExpire).toInstant()))
                .setSubject(userId)
                .claim("nickname", nickname)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
        String accessToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accessExpire).toInstant()))
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();

        Token token = new Token(refreshToken, accessToken);

        return token;
    }

    public String regenerateToken(String refreshToken) throws Exception {
        if(isValidToken(refreshToken))
            throw new RuntimeException("token is expire");

        String userId = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(refreshToken).getBody().getSubject();

        //TODO : DB의 accessToken 값과 비교하는 코드 추가 필요

        String accessToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accessExpire).toInstant()))
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();

        return accessToken;
    }

    public boolean isValidToken(String token) {
        Date expireTime = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token).getBody().getExpiration();

        return expireTime.after(new Date());
    }

    public String validateAndExtract(String token) throws Exception {
        String userId = null;

        try {
            if (!isValidToken(token))
                throw new RuntimeException("token is expire");

            userId = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token).getBody().getSubject();
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return userId;
    }
}
