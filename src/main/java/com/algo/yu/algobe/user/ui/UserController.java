package com.algo.yu.algobe.user.ui;

import com.algo.yu.algobe.food.domain.AllergyInfoDto;
import com.algo.yu.algobe.security.utils.JWTUtil;
import com.algo.yu.algobe.user.application.UserAllergyInfoService;
import com.algo.yu.algobe.user.application.UserService;
import com.algo.yu.algobe.user.domain.user.*;
import com.algo.yu.algobe.food.domain.AllergyInfo;
import com.algo.yu.algobe.user.application.TokenService;

import com.algo.yu.algobe.user.application.MailService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Map;
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
    private final TokenService tokenService;

    private final JWTUtil jwtUtil;

    @PostMapping("login")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto user) throws Exception {

        log.info("---------start login-------");
        String email = user.getUsername();
        String password = user.getPassword();

        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(email, password);
        log.info("token" + authenticationToken);

        UserAuthDto auth = null;

        try {
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            log.info("authentication" + authentication);
            auth = (UserAuthDto) authentication.getPrincipal();
            SecurityContextHolder.getContext().setAuthentication(authentication);
            if (!auth.getLoginType().equals(LoginType.LOCAL))
                throw new DisabledException("소셜로그인을 이용해주세요");
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
        Set<Grade> roles = auth.getAuthorities().stream().map(item -> Grade.valueOf(item.getAuthority().replace("ROLE_", ""))).collect(Collectors.toSet());

        log.info("-----------");
        log.info("email : " + email);
        log.info("user_id : " + userId);
        log.info("-----------");

        // 토큰 생성 및 쿠키 설정
        Token token = jwtUtil.generateToken(email, nickname);
        LoginCookies loginCookies = jwtUtil.setLoginCookies(token, roles.contains(Grade.ADMIN));

        tokenService.insertRefreshToken(userId, token.getCode().toString());

        // ResponseEntity에서 header 설정 및 만든 쿠키 넣고 응답
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE,
                        loginCookies.getAccessCookie().toString(),
                        loginCookies.getRefreshCookie().toString(),
                        loginCookies.getViewListCookie().toString(),
                        loginCookies.getFoodListCookie().toString(),
                        loginCookies.getIsLoginCookie().toString(),
                        loginCookies.getIsAdminCookie().toString())
                .body(new UserInfoResponseDto(auth.getNickname(), email, auth.getLoginType().toString(), roles.contains(Grade.ADMIN)));
    }

    @GetMapping("social")
    public ResponseEntity<SocialUserResponseDto> getSocialUserInfo(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        return ResponseEntity.ok(SocialUserResponseDto.builder()
                .nickname(auth.getNickname())
                .username(auth.getUsername())
                .build());
    }


    //TODO 회원가입 완성하기
    @Transactional
    @PostMapping("signup")
    public ResponseEntity<String> signUp(@RequestBody @Valid SignupRequestDto request) {

        if (checkUserIdDuplication(request.getUsername()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user_id is duplication.");
        if (checkNicknameDuplication(request.getNickname()).getBody().equals("true"))
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("nickname is duplication.");

        userService.signUp(request, request.getAllergyInfoDto());

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("leave")
    public ResponseEntity<String> leave(@CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth, @RequestBody String password) {
        userService.leave(auth.getUsername(), password);

        return ResponseEntity.ok("ok");
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
    public ResponseEntity<Boolean> sendMail(@RequestBody @Valid MailRequestDto mailRequestDto) {

        User auth = userService.findByUsername(mailRequestDto.getUsername());

        if(mailRequestDto.getIsSignup() && auth == null)
            mailService.sendMail(mailRequestDto.getUsername(), "신규 사용자");
        else if(!mailRequestDto.getIsSignup() && mailRequestDto.getUsername().equals(auth.getUsername()))
            mailService.sendMail(auth.getUsername(), auth.getNickname());

        return ResponseEntity.ok(true);
    }

    @PostMapping("users/validate")
    public ResponseEntity<?> validateCode(@RequestBody MailValidateRequestDto requestValidate) {
        if (requestValidate.getIsSignup() && userService.existsByUsername(requestValidate.getUsername()))
            return ResponseEntity.badRequest().body("이미 가입된 사용자입니다.");
        if (!requestValidate.getIsSignup()) {
            userService.findByUsername(requestValidate.getUsername());
        }
        boolean isValidate = mailService.validateCode(requestValidate.getUsername(), requestValidate.getCode());
        if (isValidate)
            return ResponseEntity.ok().build();
        else
            return ResponseEntity.badRequest().body("인증 시간 초과");
    }

    @PatchMapping("users/password")
    public ResponseEntity<?> changePassword(@RequestBody PasswordChangeRequestDto passwordChangeRequestDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        /* 비밀번호 변경(로그인 상태) */
        if(!passwordChangeRequestDto.getIsReset() && !passwordChangeRequestDto.getUsername().equals(auth.getUsername()))
            return ResponseEntity.badRequest().body("요청 아이디 미일치");

        User user = userService.findByUsername(passwordChangeRequestDto.getUsername());
        if (user == null)
            return ResponseEntity.badRequest().body("없는 유저입니다");
        /* 로컬 유저 여부 확인 */
        if (!user.getLoginType().equals(LoginType.LOCAL))
            return ResponseEntity.badRequest().body("소셜 회원은 비밀번호를 변경할 수 없습니다");
        /* 비밀번호 찾기시 */
        if (passwordChangeRequestDto.getIsReset()) {
            if (auth != null)
                return ResponseEntity.badRequest().body("로그아웃하고 비밀번호 찾기를 시도해주세요");
            if (!mailService.isValidateUser(passwordChangeRequestDto.getUsername()))
                return ResponseEntity.badRequest().body("인증 시간 초과");
        }

        userService.passwordChange(user, passwordChangeRequestDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("users/nickname")
    public ResponseEntity<String> changeNickname(@RequestBody Map<String, String> map, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        String nickname = map.get("nickname");

        nickname = nickname.trim();
        /* 기존 닉네임과 일치 여부 확인 */
        if (auth.getNickname().equals(nickname))
            return ResponseEntity.badRequest().body("기존 닉네임 입니다");
        if (userService.existsByNickname(nickname))
            return ResponseEntity.badRequest().body("이미 존재하는 닉네임 입니다");

        userService.nicknameChange(auth, nickname);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("users/introduce")
    public ResponseEntity<Void> changeIntroduce(@RequestBody Map<String, String> map, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        String introduce = map.get("introduce");
        introduce = introduce.trim();
        userService.introduceChange(auth, introduce);

        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("users/allergies")
    public ResponseEntity<?> changeAllergy(@RequestBody AllergyInfoDto allergyInfoDto, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        userService.allergyChange(auth, allergyInfoDto);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("users/profile_images")
    public ResponseEntity<?> updateProfileImage(@RequestBody Map<String, String> profileImageUrl,
                                                                            @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        String imageUrl = profileImageUrl.get("profileImageUrl");
        userService.profileImageChange(auth, imageUrl);
        return ResponseEntity.ok().build();
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("users/allergies")
    public ResponseEntity<Map<String, Boolean>> getUserAllergyInfo(@Parameter(hidden = true) @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        return ResponseEntity.ok(userAllergyInfoService.findUserAllergyInfoByUser(auth));
    }

//    @PatchMapping("/users/")
//    public ResponseEntity<Long> updateProfile(UserProfileDto userProfileDto , @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
//
//    }

}
