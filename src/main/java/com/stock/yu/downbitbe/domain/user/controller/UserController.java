package com.stock.yu.downbitbe.domain.user.controller;

import com.stock.yu.downbitbe.domain.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.domain.user.entity.Grade;
import com.stock.yu.downbitbe.domain.user.entity.LoginType;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.stock.yu.downbitbe.domain.user.repository.CustomUserRepository;
import com.stock.yu.downbitbe.security.payload.request.LoginRequest;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v2")
public class UserController {


    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;
    private final CustomUserRepository repository;
    private JWTUtil jwtUtil;

    // ���� ������ ��ū ��ȯ���ָ� ��
    @PostMapping("/login")
    //public ResponseEntity<Void> login(@RequestBody Map<String, String> user) throws Exception {
    public ResponseEntity<String> login(@RequestBody LoginRequest user) throws Exception {
        // ���Ե��� ���� ȸ������ Ȯ��

        //String email = user.get("username");
        //String password = user.get("password");
        log.info("---------start login-------");
        String email = user.getUsername();
        String password = user.getPassword();

        UsernamePasswordAuthenticationToken token = new UsernamePasswordAuthenticationToken(email, password);
        log.info("token" + token);

        try {
            // AuthenticationManager �� token �� �ѱ�� UserDetailsService �� �޾� ó���ϵ��� �Ѵ�.
            Authentication authentication = authenticationManager.authenticate(token);
            log.info("authentication"+authentication);
            // ���� SecurityContext �� authentication ������ ����Ѵ�.
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } catch (DisabledException | LockedException | BadCredentialsException e) {
            String status;
            if (e.getClass().equals(BadCredentialsException.class)) {
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
//        Authentication authentication = authenticationManager.authenticate(token);
//        log.info("authentication"+authentication);
//        SecurityContextHolder.getContext().setAuthentication(authentication);
//        log.info("context done");
//        String jwt = jwtUtil.generateToken(email);
//
//        UserAuthDTO userAuthDTO = (UserAuthDTO) authentication.getPrincipal();
//
//        List<String> roles = userAuthDTO.getAuthorities().stream().map(item -> item.getAuthority()).collect(Collectors.toList());

        log.info("-----------");
        log.info("email : " + email);
        log.info("password : " + password);
        log.info("-----------");

        if(!passwordEncoder.matches(password, repository.findByUserId(email).getPassword())) {
            throw new IllegalArgumentException("��й�ȣ ����ġ");
        }

        // ��ū ���� �� ��Ű ����


        // ResponseEntity���� header ���� �� ���� ��Ű �ְ� ����
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    //TODO ȸ������ �ϼ��ϱ�
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
