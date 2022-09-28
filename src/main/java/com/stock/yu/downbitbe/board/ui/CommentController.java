package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.CommentService;
import com.stock.yu.downbitbe.board.domain.comment.Comment;
import com.stock.yu.downbitbe.board.domain.comment.CommentDto;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
public class CommentController {
    private final CommentService commentService;
    private final CustomUserRepository userRepository;

    @GetMapping("/api/v1/boards/{board_id}/posts/{post_id}/comments")
    public ResponseEntity<?> getCommentList(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId, @PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable){
        Page<Comment> commentListResponse = commentService.findAllCommentsByPostId(postId, pageable);
        return ResponseEntity.status(HttpStatus.OK).body(commentListResponse.stream().map(CommentDto::new).collect(Collectors.toList()));
    }
}