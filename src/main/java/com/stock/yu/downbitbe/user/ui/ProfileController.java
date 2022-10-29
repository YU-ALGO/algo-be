package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.user.application.ProfileService;
import com.stock.yu.downbitbe.user.application.UserAllergyInfoService;
import com.stock.yu.downbitbe.user.application.UserService;
import com.stock.yu.downbitbe.user.domain.profile.ProfileCommentDto;
import com.stock.yu.downbitbe.user.domain.profile.ProfilePostDto;
import com.stock.yu.downbitbe.user.domain.profile.UserProfileDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.domain.user.UserAllergyInfo;
import com.stock.yu.downbitbe.user.domain.user.UserAllergyInfoRepository;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    private final UserService userService;
    private final ProfileService profileService;

    @GetMapping("/{nickname}")
    public ResponseEntity<UserProfileDto> getUserInfo(@RequestBody String nickname, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        return ResponseEntity.ok(profileService.getUserProfileByNickname(nickname, auth.getNickname().equals(nickname)));
    }

    @GetMapping("/{nickname}/posts")
    public ResponseEntity<List<ProfilePostDto>> getUserPosts(@RequestBody String nickname) {
        return ResponseEntity.ok(profileService.getUserPostsByNickname(nickname));
    }

    @GetMapping("/{nickname}/comments")
    public ResponseEntity<List<ProfileCommentDto>> getUserCommnets(@RequestBody String nickname) {
        return ResponseEntity.ok(profileService.getUserCommentsByNickname(nickname));
    }
}
