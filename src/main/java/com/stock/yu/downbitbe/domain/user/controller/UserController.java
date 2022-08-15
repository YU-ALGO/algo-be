package com.stock.yu.downbitbe.domain.user.controller;

import com.stock.yu.downbitbe.domain.user.entity.Grade;
import com.stock.yu.downbitbe.domain.user.entity.LoginType;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
public class UserController {

    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository repository;

    // 리턴 값으로 토큰 반환해주면 됨
    @PostMapping("/api/v2/login")
    public ResponseEntity<Void> login(@RequestBody Map<String, String> user) {
        // 가입되지 않은 회원인지 확인
        String email = user.get("username");
        String password = user.get("password");

        log.info("-----------");
        log.info("email : " + email);
        log.info("password : " + password);
        log.info("-----------");

        if(!passwordEncoder.matches(password, repository.findByUserId(email).getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        // 토큰 생성 및 쿠키 설정

        // ResponseEntity에서 header 설정 및 만든 쿠키 넣고 응답
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //TODO 회원가입 완성하기
    @Transactional
    @PostMapping("/api/v2/signup")
    public ResponseEntity<String> signUp(@RequestBody Map<String, String> form) {
        String userId = form.get("user_id");
        String password = passwordEncoder.encode(form.get("password"));
        String nickname = form.get("nickname");

        if(checkUserIdDuplication(userId).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_id is duplication.");
        if(checkNicknameDuplication(nickname).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nickname is duplication.");

        User newUser = User.builder()
                .userId(userId)
                .password(password)
                .nickname(nickname)
                .type(LoginType.LOCAL)
                .hitRate(0)
                .build();

        newUser.addGrade(Grade.USER);
        repository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

//    @GetMapping("api/v2/users/exists")
//    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestParam("user_id") String UserId)
    @GetMapping("/api/v2/users/{user_id}/exists")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestBody @PathVariable("user_id") String userId) {
        User user = repository.findByUserId(userId);

        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);

    }

    @GetMapping("/api/v2/users/exists")
    public ResponseEntity<Boolean> checkNicknameDuplication(@RequestParam("nickname") String nickname) {
        boolean isExist = repository.existsByNickname(nickname);

        if(isExist)
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    /*
    * Get version *
    @GetMapping("/api/v2/users/{user_id}/exists")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestBody @PathVariable("user_id") String userId) {
        User user = repository.findByUserId(userId);

        if(user != null) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);

    }

    @GetMapping("/api/v2/users/exists")
    public ResponseEntity<Boolean> checkUserNicknameDuplication(@RequestParam("nickname") String nickname) {
        boolean isExist = repository.existsByNickname(nickname);

        if(isExist)
            ResponseEntity.status(HttpStatus.OK).body(true);
        else
            ResponseEntity.status(HttpStatus.OK).body(false);
    }*/

}
