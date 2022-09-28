package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.PostLikeService;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards/")
public class PostLikeController {
    private final PostLikeService postLikeService;
    private final CustomUserRepository userRepository;

    @PostMapping("{board_id}/posts/{post_id}/likes")
    public ResponseEntity<?> createPostLike(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                                            @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userRepository.findByUsername(auth.getUsername());
        Long ret = postLikeService.createPostLike(boardId, postId, user);
        postLikeService.updateLike(postId, user,1);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @DeleteMapping("{board_id}/posts/{post_id}/likes")
    public ResponseEntity<?> deletePostLike(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                                            @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userRepository.findByUsername(auth.getUsername());
        Long ret = postLikeService.deletePostLike(boardId, postId, user);
        postLikeService.updateLike(postId, user,-1);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

}