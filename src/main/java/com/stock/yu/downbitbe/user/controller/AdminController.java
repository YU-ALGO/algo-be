package com.stock.yu.downbitbe.user.controller;


import com.stock.yu.downbitbe.user.entity.Grade;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {

    private final JWTUtil jwtUtil;
    private final CustomUserRepository repository;

    @GetMapping("/admin")
    public ResponseEntity<?> isAdmin(@CookieValue("accessToken")String accessToken) {
        try {
            String username = jwtUtil.validateAndExtract(accessToken);
            User user = repository.findByUsername(username);
            if(user.getGradeSet().contains(Grade.ADMIN))
                return ResponseEntity.ok().build();
            else
                return ResponseEntity.badRequest().body("NOT ADMIN");
        }
        catch (Exception e) {
            e.printStackTrace();

            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }
}
