package com.stock.yu.downbitbe.security.controller;

import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.service.PrincipalDetails;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import java.security.Principal;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v2/token")
public class TokenController {

    private final JWTUtil jwtUtil;

    @PostMapping(value = "/validate")
    public ResponseEntity<?> validateToken(@RequestBody String accessToken, @CurrentSecurityContext(expression = "authentication.principal")
    UserAuthDTO principal) throws Exception {
        UserAuthDTO user = principal;

        log.info("user_id : " + user.getUserId());
        log.info("nickname : " + user.getNickname());
        log.info("password : " + user.getPassword());

        //String value = jwtUtil.validateAndExtract(accessToken);

        //log.info("value : " + value);
        return ResponseEntity.ok().build();
    }


}
