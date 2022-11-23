package com.stock.yu.downbitbe.user.ui;

import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.food.domain.FoodResponseDto;
import com.stock.yu.downbitbe.user.application.ProfileService;
import com.stock.yu.downbitbe.user.application.UserService;
import com.stock.yu.downbitbe.user.domain.profile.ProfileCommentDto;
import com.stock.yu.downbitbe.user.domain.profile.ProfilePostDto;
import com.stock.yu.downbitbe.user.domain.profile.UserFoodLikeResponseDto;
import com.stock.yu.downbitbe.user.domain.profile.UserProfileDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
@RestController
@PreAuthorize("isAuthenticated()")
@RequiredArgsConstructor
@RequestMapping("/api/v1/profiles")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
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
    public ResponseEntity<List<ProfilePostDto>> getUserPosts(@PathVariable String nickname,
                                                             @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProfilePostDto> profilePostList = profileService.getUserPostsByNickname(nickname, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(profilePostList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(profilePostList.stream().collect(Collectors.toList()));
    }

    //@PreAuthorize("isAuthenticated()")
    @Operation(summary = "해당 닉네임 사용자가 작성한 댓글들")
    @GetMapping("/{nickname}/comments")
    public ResponseEntity<List<ProfileCommentDto>> getUserComments(@PathVariable String nickname,
                                                                   @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<ProfileCommentDto> profileCommentList = profileService.getUserCommentsByNickname(nickname, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(profileCommentList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(profileCommentList.stream().collect(Collectors.toList()));
    }

    //@PreAuthorize("isAuthenticated()")
    @GetMapping("/{nickname}/foods")
    public ResponseEntity<List<UserFoodLikeResponseDto>> getUserFoodLikeList(@PathVariable String nickname,
                                                                             @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable) {
        Page<UserFoodLikeResponseDto> userFoodLikeList = profileService.getUserFoodLikeListByNickname(nickname, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(userFoodLikeList.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(userFoodLikeList.stream().collect(Collectors.toList()));
    }
}
