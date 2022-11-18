package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.food.domain.FoodResponseDto;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@Log4j2
@RestController
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
public class ProfileController {
    private final UserService userService;
    private final ProfileService profileService;

    //@PreAuthorize("isAuthenticated()")
    @Operation(summary = "해당 닉네임 사용자 프로필 가져오기")
    //TODO 시큐리티 관련 swagger 지우는 법
    @GetMapping("/{nickname}")
    public ResponseEntity<UserProfileDto> getUserInfo(@PathVariable String nickname, @Parameter(hidden = true) @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        return ResponseEntity.ok(profileService.getUserProfileByNickname(nickname, auth.getNickname().equals(nickname)));
    }

    //@PreAuthorize("isAuthenticated()")
    @Operation(summary = "해당 닉네임 사용자가 작성한 게시글들")
    @GetMapping("/{nickname}/posts")
    public ResponseEntity<List<ProfilePostDto>> getUserPosts(@PathVariable String nickname) {
        return ResponseEntity.ok(profileService.getUserPostsByNickname(nickname));
    }

    //@PreAuthorize("isAuthenticated()")
    @Operation(summary = "해당 닉네임 사용자가 작성한 댓글들")
    @GetMapping("/{nickname}/comments")
    public ResponseEntity<List<ProfileCommentDto>> getUserComments(@PathVariable String nickname) {
        return ResponseEntity.ok(profileService.getUserCommentsByNickname(nickname));
    }

    //@PreAuthorize("isAuthenticated()")
//    @GetMapping("/{nickname}/foods")//TODO
//    public ResponseEntity<List<FoodResponseDto>> getUserFoodLikeList(@PathVariable String nickname) {
//        return ResponseEntity.ok(profileService.(nickname));
//    }
}
