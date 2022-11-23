package com.algo.yu.algobe.board.ui;

import com.algo.yu.algobe.board.application.PostLikeService;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.application.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards/")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @PostMapping("{board_id}/posts/{post_id}/likes")
    public ResponseEntity<Long> createPostLike(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                                            @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = postLikeService.createPostLike(boardId, postId, user);
        postLikeService.updateLike(postId, user,1);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("{board_id}/posts/{post_id}/likes")
    public ResponseEntity<Long> deletePostLike(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                                            @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = postLikeService.deletePostLike(boardId, postId, user);
        postLikeService.updateLike(postId, user,-1);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

}
