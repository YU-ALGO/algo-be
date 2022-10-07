package com.stock.yu.downbitbe.user.controller;

import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.stock.yu.downbitbe.user.dto.LoginCookiesDTO;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.Token;
import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.user.entity.Grade;
import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.security.payload.request.LoginRequest;
import com.stock.yu.downbitbe.security.payload.request.SignupRequest;
import com.stock.yu.downbitbe.security.payload.response.JwtResponse;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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

import java.util.Set;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class UserController {

    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository repository;

    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    //public ResponseEntity<Void> login(@RequestBody Map<String, String> user) throws Exception {
    public ResponseEntity<?> login(@RequestBody LoginRequest user) throws Exception {

        log.info("---------start login-------");
        String email = user.getUsername();
        String password = user.getPassword();
        log.info("username : " + user.getUsername());
        log.info("password : " + user.getPassword());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("token" + authenticationToken);

        UserAuthDTO auth = null;

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("authentication"+authentication);
            auth = (UserAuthDTO) authentication.getPrincipal();
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
        String nickname = auth.getNickname();
        Set<Grade> roles = auth.getAuthorities().stream().map(item -> Grade.valueOf(item.getAuthority().replace("ROLE_",""))).collect(Collectors.toSet());

        log.info("-----------");
        log.info("email : " + email);
        log.info("user_id : " + userId);
        log.info("password : " + password);
        log.info("-----------");

        Token token = jwtUtil.generateToken(email, nickname);
        LoginCookiesDTO loginCookiesDTO = jwtUtil.setLoginCookies(token, roles.contains(Grade.ADMIN));

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        loginCookiesDTO.getAccessCookie().toString(),
                        loginCookiesDTO.getRefreshCookie().toString(),
                        loginCookiesDTO.getViewListCookie().toString(),
                        loginCookiesDTO.getIsLoginCookie().toString(),
                        loginCookiesDTO.getIsAdminCookie().toString())
                .body(JwtResponse.builder()
                        .nickname(auth.getNickname())
                        .username(email)
                        .loginType(auth.getLoginType().toString())
                        .isAdmin(roles.contains(Grade.ADMIN))
                        .build());
    }

    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequest request) {

        if(checkUserIdDuplication(request.getUsername()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_id is duplication.");
        if(checkNicknameDuplication(request.getNickname()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nickname is duplication.");

        User newUser = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .nickname(request.getNickname())
                .loginType(LoginType.LOCAL)
                .build();

        newUser.addGrade(Grade.USER);
        repository.save(newUser);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{username}/exists")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestBody @PathVariable("username") String username) {
        boolean user = repository.existsByUsername(username);

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

}
