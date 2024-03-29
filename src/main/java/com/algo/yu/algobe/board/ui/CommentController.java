package com.algo.yu.algobe.board.ui;

import com.algo.yu.algobe.board.application.CommentService;
import com.algo.yu.algobe.board.domain.comment.CommentCreateRequestDto;
import com.algo.yu.algobe.board.domain.comment.CommentUpdateRequestDto;
import com.algo.yu.algobe.board.domain.comment.Comment;
import com.algo.yu.algobe.board.domain.comment.CommentDto;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.application.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
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

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class CommentController {
    private final CommentService commentService;
    private final UserService userService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{board_id}/posts/{post_id}/comments")
    public ResponseEntity<List<CommentDto>> getCommentList(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Comment> commentListResponse = commentService.findAllCommentsByPostId(postId, pageable);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(commentListResponse.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(commentListResponse.stream().map(CommentDto::new).collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{board_id}/posts/{post_id}/comments")
    public ResponseEntity<?> createComment(final @RequestBody @Valid CommentCreateRequestDto commentCreateRequestDto, @PathVariable("board_id") Long boardId,
                                           @PathVariable("post_id") Long postId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long commentId = commentService.createComment(commentCreateRequestDto, postId, user);
        if(commentId == null){ // TODO : 실패시 반환값 확인후 조건문 다시 작성
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        commentService.updateCommentCount(postId, user, 1);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PreAuthorize("isAuthenticated()")
    @PatchMapping("/{board_id}/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<?> updateComment(final @RequestBody @Valid CommentUpdateRequestDto commentUpdateRequestDto, @PathVariable("board_id") Long boardId,
                                           @PathVariable("post_id") Long postId, @PathVariable("comment_id") Long commentId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long updateResult = commentService.updateComment(commentUpdateRequestDto, postId, commentId, user);
        if(updateResult == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{board_id}/posts/{post_id}/comments/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId, @PathVariable("comment_id") Long commentId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        Long deleteResult = commentService.deleteComment(postId, commentId, user);
        if(deleteResult == null){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
        commentService.updateCommentCount(postId, user, -1);
        return ResponseEntity.status(HttpStatus.OK).body(true);
    }

}