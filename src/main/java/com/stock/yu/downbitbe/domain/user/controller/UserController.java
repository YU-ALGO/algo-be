package com.stock.yu.downbitbe.domain.user.controller;

import com.stock.yu.downbitbe.config.Config;
import com.stock.yu.downbitbe.domain.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.domain.user.entity.Grade;
import com.stock.yu.downbitbe.domain.user.entity.LoginType;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.security.payload.request.LoginRequest;
import com.stock.yu.downbitbe.security.payload.response.JwtResponse;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.web.server.Cookie;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class UserController {


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository repository;

    private final JWTUtil jwtUtil;

    // 리턴 값으로 토큰 반환해주면 됨
    //@CrossOrigin(origins = "http://downbit.r-e.kr:3000", allowCredentials = "true")
    @PostMapping("/login")
    //public ResponseEntity<Void> login(@RequestBody Map<String, String> user) throws Exception {
    public ResponseEntity<?> login(@RequestBody LoginRequest user) throws Exception {
        // 가입되지 않은 회원인지 확인

        //String email = user.get("username");
        //String password = user.get("password");
        log.info("---------start login-------");
        String email = user.getUsername();
        String password = user.getPassword();
        log.info("username : " + user.getUsername());
        log.info("password : " + user.getPassword());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("token" + authenticationToken);

        try {
            // AuthenticationManager 에 token 을 넘기면 UserDetailsService 가 받아 처리하도록 한다.
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("authentication"+authentication);
            // 실제 SecurityContext 에 authentication 정보를 등록한다.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException | LockedException | BadCredentialsException e) {
            String status;
            if (e.getClass().equals(BadCredentialsException.class)) {
                log.info(((BadCredentialsException) e).getClass().getName());
                status = "invalid-password";
            } else if (e.getClass().equals(DisabledException.class)) {
                status = "locked";
            } else if (e.getClass().equals(LockedException.class)) {
                status = "disable";
            } else {
                status = "unknown";
            }


            log.info(status);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(status);
        }

        String userId = (String) authenticationToken.getPrincipal();
        Set<Grade> roles = repository.findByUserId(userId).getGradeSet();
        //Set<Grade> roles = userAuthDTO.getAuthorities().stream().map(item -> Grade.valueOf(item.getAuthority())).collect(Collectors.toSet());



/*        Authentication authentication = authenticationManager.authenticate(token);
        log.info("authentication"+authentication);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        log.info("context done");
        String jwt = jwtUtil.generateToken(email);

        UserAuthDTO userAuthDTO = (UserAuthDTO) authentication.getPrincipal();

        List<String> roles = userAuthDTO.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());*/

        log.info("-----------");
        log.info("email : " + email);
        log.info("user_id : " + userId);
        log.info("password : " + password);
        log.info("-----------");

        if(!passwordEncoder.matches(password, repository.findByUserId(email).getPassword())) {
            throw new IllegalArgumentException("비밀번호 불일치");
        }

        // 토큰 생성 및 쿠키 설정
        ResponseCookie responseCookie = ResponseCookie.from("token",jwtUtil.generateToken(email))
                .httpOnly(true)
                .path("/")
                //.sameSite("none")
                .maxAge(600L)
                .domain(Config.DOMAIN)
                .build();


        // ResponseEntity에서 header 설정 및 만든 쿠키 넣고 응답
        //return ResponseEntity.status(HttpStatus.OK).build();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, responseCookie.toString())
                .body(JwtResponse.builder()
                .userId(email)
                .type(repository.findByUserId(email).getType().toString())
                .roles(roles)
                .build());
    }

    //TODO 회원가입 완성하기
    @Transactional
    @PostMapping("/signup")
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
    @GetMapping("/users/{user_id}/exists")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestBody @PathVariable("user_id") String userId) {
        boolean user = repository.existsByUserId(userId);

        if(user) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);

    }

    @GetMapping("/users/exists")
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
