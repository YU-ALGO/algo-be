package com.stock.yu.downbitbe.security.utils;

import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.user.application.TokenService;
import com.stock.yu.downbitbe.user.domain.user.LoginCookies;
import com.stock.yu.downbitbe.user.domain.user.Token;
import com.stock.yu.downbitbe.user.utils.RandomCodeUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.joda.time.DateTime;
import org.springframework.http.ResponseCookie;

import java.nio.charset.StandardCharsets;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
@RequiredArgsConstructor
public class JWTUtil {
    private String secretKey = "algo"; // 실무에서는 유저의 비밀번호 등을 이용해서 넣어주는게 맞음

    public static long accessExpire = 60 * 10; //10분
    public static long refreshExpire = 60 * 60 * 24; //24시간
    public final TokenService tokenService;

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
        int code = RandomCodeUtil.createCode();
        String refreshToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(refreshExpire).toInstant()))
                .setSubject(userId)
                .claim("code", code)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();
        String accessToken = Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(accessExpire).toInstant()))
                .setSubject(userId)
                .signWith(SignatureAlgorithm.HS256, secretKey.getBytes(StandardCharsets.UTF_8))
                .compact();

        Token token = new Token(refreshToken, accessToken, code);

        return token;
    }

    public String regenerateToken(String refreshToken) throws Exception {
        if(!isValidToken(refreshToken))
            throw new RuntimeException("token is expire");

        String userId = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(refreshToken).getBody().getSubject();
        Integer code = Jwts.parser().setSigningKey(secretKey.getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(refreshToken).getBody().get("code", Integer.class);

        if(!tokenService.validateRefreshToken(userId, code.toString()))
            throw new RuntimeException("토큰 미일치");

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
        log.info("expireTime : " + expireTime);
        log.info("now() : " + DateTime.now().toDate());
        return expireTime.after(DateTime.now().toDate());
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

    public LoginCookies setLoginCookies(Token token, boolean isAdmin) throws Exception {
        ResponseCookie accessCookie = ResponseCookie.from("accessToken",token.getAccessToken())
                .httpOnly(true)
                .path("/")
                //.sameSite("none")
                .maxAge(JWTUtil.accessExpire)
                .domain(Config.DOMAIN)
                .build();

        ResponseCookie refreshCookie = ResponseCookie.from("refreshToken",token.getRefreshToken())
                .httpOnly(true)
                .path("/")
                //.sameSite("none")
                .maxAge(JWTUtil.refreshExpire)
                .domain(Config.DOMAIN)
                .build();

        ResponseCookie viewCookie = ResponseCookie.from("viewList", "")
                .httpOnly(true)
                .path("/")
                .maxAge(JWTUtil.accessExpire)
                .domain(Config.DOMAIN)
                .build();

        ResponseCookie foodCookie = ResponseCookie.from("foodList", "")
                .httpOnly(true)
                .path("/")
                .maxAge(JWTUtil.accessExpire)
                .domain(Config.DOMAIN)
                .build();

        ResponseCookie isLoginCookie = ResponseCookie.from("isLogin", "true")
                .path("/")
                .maxAge(JWTUtil.refreshExpire)
                .domain(Config.DOMAIN)
                .build();

        ResponseCookie isAdminCookie = ResponseCookie.from("isAdmin", String.valueOf(isAdmin))
                .path("/")
                .maxAge(JWTUtil.refreshExpire)
                .domain(Config.DOMAIN)
                .build();

        return LoginCookies.builder()
                .accessCookie(accessCookie)
                .refreshCookie(refreshCookie)
                .viewListCookie(viewCookie)
                .foodListCookie(foodCookie)
                .isLoginCookie(isLoginCookie)
                .isAdminCookie(isAdminCookie)
                .build();
    }
}
