package com.algo.yu.algobe.user.ui;


import com.algo.yu.algobe.security.utils.JWTUtil;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.domain.user.Grade;
import com.algo.yu.algobe.user.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class AdminController {

    private final JWTUtil jwtUtil;
    private final UserService userService;

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/admin")
    public ResponseEntity<?> isAdmin(@CookieValue("accessToken")String accessToken) {
        try {
            String username = jwtUtil.validateAndExtract(accessToken);
            User user = userService.findByUsername(username);
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
