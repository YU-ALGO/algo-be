package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.stock.yu.downbitbe.user.domain.profile.UserProfileDto;
import com.stock.yu.downbitbe.user.domain.user.LoginRequestDto;
import com.stock.yu.downbitbe.user.domain.user.SignupRequestDto;
import com.stock.yu.downbitbe.user.domain.user.UserInfoResponseDto;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import com.stock.yu.downbitbe.user.domain.user.LoginCookies;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
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

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto user) throws Exception {

        log.info("---------start login-------");
        String email = user.getUsername();
        String password = user.getPassword();
        log.info("username : " + user.getUsername());
        log.info("password : " + user.getPassword());

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("token" + authenticationToken);

        UserAuthDto auth = null;

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("authentication" + authentication);
            auth = (UserAuthDto) authentication.getPrincipal();
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

        // 토큰 생성 및 쿠키 설정
        Token token = jwtUtil.generateToken(email, nickname);
        LoginCookies loginCookies = jwtUtil.setLoginCookies(token, roles.contains(Grade.ADMIN));

        // ResponseEntity에서 header 설정 및 만든 쿠키 넣고 응답
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        loginCookies.getAccessCookie().toString(),
                        loginCookies.getRefreshCookie().toString(),
                        loginCookies.getViewListCookie().toString(),
                        loginCookies.getIsLoginCookie().toString(),
                        loginCookies.getIsAdminCookie().toString())
                .body(new UserInfoResponseDto(auth.getNickname(), email, auth.getLoginType().toString(), roles.contains(Grade.ADMIN)));
    }

    //TODO 회원가입 완성하기
    @Transactional
    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody SignupRequestDto request) {

        if(checkUserIdDuplication(request.getUsername()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_id is duplication.");
        if (checkNicknameDuplication(request.getNickname()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nickname is duplication.");

        userService.signUp(request);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("users/{username}/exists")
    public ResponseEntity<Boolean> checkUserIdDuplication(@RequestBody @PathVariable("username") String username) {
        boolean user = userService.existsByUsername(username);

        if (user) {
            return ResponseEntity.status(HttpStatus.OK).body(true);
        }

        return ResponseEntity.status(HttpStatus.OK).body(false);

    }

    @GetMapping("users/exists")
    public ResponseEntity<Boolean> checkNicknameDuplication(@RequestParam("nickname") String nickname) {
        boolean isExist = userService.existsByNickname(nickname);

        if (isExist)
            return ResponseEntity.status(HttpStatus.OK).body(true);
        else
            return ResponseEntity.status(HttpStatus.OK).body(false);
    }

    @PostMapping("users/mail")
    public ResponseEntity<Boolean> sendMail(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        mailService.sendMail(userService.findByUsername(auth.getUsername()));

        return ResponseEntity.ok(true);
    }

    @GetMapping("users/validate")
    public ResponseEntity<?> validateCode(@RequestParam("code") int code, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        boolean isValidate = mailService.validateCode(userService.findByUsername(auth.getUsername()), code);
        if (isValidate)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("인증 시간 초과");
    }

    @PatchMapping("users/password")
    public ResponseEntity<?> changePassword(@RequestBody @PathVariable("newPassword") String newPassword, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        /* 로컬 유저 여부 확인 */
        if(!auth.getLoginType().equals(LoginType.LOCAL))
            return ResponseEntity.badRequest().body("소셜 회원은 비밀번호를 변경할 수 없습니다");

        userService.passwordChange(auth, newPassword);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("users/nickname")
    public ResponseEntity<?> changeNickname(@RequestBody String nickname, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        nickname = nickname.trim();
        /* 기존 닉네임과 일치 여부 확인 */
        if(auth.getNickname().equals(nickname))
            return ResponseEntity.badRequest().body("기존 닉네임 입니다");
        if(userService.existsByNickname(nickname))
            return ResponseEntity.badRequest().body("이미 존재하는 닉네임 입니다");

        userService.nicknameChange(auth, nickname);
        return ResponseEntity.ok().build();
    }

    @PatchMapping("users/introduce")
    public ResponseEntity<?> changeIntroduce(@RequestBody String introduce, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        introduce = introduce.trim();
        userService.introduceChange(auth, introduce);

        return ResponseEntity.ok().build();
    }

    @PatchMapping("users/allergy")
    public ResponseEntity<?> changeAllergy(@RequestBody AllergyInfo allergyInfo, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {

        userService.allergyChange(auth, allergyInfo);

        return ResponseEntity.ok().build();
    }


//    @PatchMapping("/users/")
//    public ResponseEntity<Long> updateProfile(UserProfileDto userProfileDto , @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
//
//    }

}
