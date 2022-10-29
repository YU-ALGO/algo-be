package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.user.domain.user.LoginRequestDto;
import com.stock.yu.downbitbe.user.domain.user.SignupRequestDto;
import com.stock.yu.downbitbe.user.domain.user.UserInfoResponseDto;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import com.stock.yu.downbitbe.user.domain.user.LoginCookiesDTO;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDTO;
import com.stock.yu.downbitbe.user.domain.user.Grade;
import com.stock.yu.downbitbe.user.domain.user.LoginType;
import com.stock.yu.downbitbe.user.domain.user.Token;
import com.stock.yu.downbitbe.user.application.MailService;
import com.stock.yu.downbitbe.user.application.UserAllergyInfoService;
import com.stock.yu.downbitbe.user.application.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private final MailService mailService;
    private final UserService userService;
    private final UserAllergyInfoService userAllergyInfoService;

    private final JWTUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto user) throws Exception {

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
            log.info("authentication" + authentication);
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

        // ��ū ���� �� ��Ű ����
        Token token = jwtUtil.generateToken(email, nickname);
        LoginCookiesDTO loginCookiesDTO = jwtUtil.setLoginCookies(token, roles.contains(Grade.ADMIN));

        // ResponseEntity���� header ���� �� ���� ��Ű �ְ� ����
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        loginCookiesDTO.getAccessCookie().toString(),
                        loginCookiesDTO.getRefreshCookie().toString(),
                        loginCookiesDTO.getViewListCookie().toString(),
                        loginCookiesDTO.getIsLoginCookie().toString(),
                        loginCookiesDTO.getIsAdminCookie().toString())
                .body(UserInfoResponseDto.builder()
                        .nickname(auth.getNickname())
                        .username(email)
                        .loginType(auth.getLoginType().toString())
                        .isAdmin(roles.contains(Grade.ADMIN))
                        .build());
    }

    //TODO ȸ������ �ϼ��ϱ�
    @Transactional
    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto request) {

        if(checkUserIdDuplication(request.getUsername()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_id is duplication.");
        if (checkNicknameDuplication(request.getNickname()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nickname is duplication.");

        userService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/users/{username}/exists")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestBody @PathVariable("username") String username) {
        boolean user = userService.existsByUsername(username);

        if (user) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);

    }

    @GetMapping("/users/exists")
    public ResponseEntity<Boolean> checkNicknameDuplication(@RequestParam("nickname") String nickname) {
        boolean isExist = userService.existsByNickname(nickname);

        if (isExist)
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @PostMapping("/users/mail")
    public ResponseEntity<Boolean> sendMail(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth) {
        mailService.sendMail(userService.findByUsername(auth.getUsername()));

        return ResponseEntity.ok(true);
    }

    @GetMapping("/users/validate")
    public ResponseEntity<?> validateCode(@RequestParam("code") int code, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth) {
        boolean isValidate = mailService.validateCode(userService.findByUsername(auth.getUsername()), code);
        if (isValidate)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("���� �ð� �ʰ�");
    }

    @PostMapping("/users/newpassword")
    public ResponseEntity<?> changePassword(@RequestBody @PathVariable("newPassword") String newPassword, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth) {
        /* ���� ���� ���� Ȯ�� */
        if(!auth.getLoginType().equals(LoginType.LOCAL))
            return ResponseEntity.badRequest().body("�Ҽ� ȸ���� ��й�ȣ�� ������ �� �����ϴ�");

        userService.passwordChange(auth, newPassword);
        return ResponseEntity.ok().build();
    }

}
