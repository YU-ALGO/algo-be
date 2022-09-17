package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.PostService;
import com.stock.yu.downbitbe.board.domain.post.PostCreateRequestDto;
import com.stock.yu.downbitbe.board.domain.post.PostResponseDto;
import com.stock.yu.downbitbe.board.domain.post.PostListResponseDto;
import com.stock.yu.downbitbe.board.domain.post.PostUpdateRequestDto;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.repository.CustomUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
@Log4j2
public class PostController {
    private final PostService postService;
    private final CustomUserRepository userRepository;


    // TODO : query 전용 repository 생성시 성능 향상
    @GetMapping("/{board_id}/posts")
    public ResponseEntity<List<PostListResponseDto>> posts(@PathVariable("board_id") Long boardId, @RequestParam int page, @RequestParam int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        return ResponseEntity.status(HttpStatus.OK).body(postService.findAllPostsById(boardId, pageRequest));
    }

    @GetMapping("/{board_id}/posts/{post_id}")
    public ResponseEntity<PostResponseDto> post(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId) {
        PostResponseDto responseDto = postService.findPostByPostId(boardId, postId);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    @PostMapping("/{board_id}/posts")
    public Long createPost(@RequestBody @Valid PostCreateRequestDto postCreateRequestDto, @PathVariable("board_id") Long boardId,
                           @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth) {
        User user = userRepository.findByUsername(auth.getUsername());

        return postService.createPost(postCreateRequestDto, boardId, user);
    }

    @PatchMapping("/{board_id}/posts/{post_id}")
    public Long updatePost(@RequestBody @Valid PostUpdateRequestDto postUpdateRequestDto, @PathVariable("board_id") Long boardId,
                           @PathVariable("post_id") Long postId, @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth) {
        User user = userRepository.findByUsername(auth.getUsername());
        return postService.updatePost(postUpdateRequestDto, boardId, postId, user);
    }

    @DeleteMapping("/{board_id}/posts/{post_id}")
    public Long deletePost(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId,
                           @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth) {
        User user = userRepository.findByUsername(auth.getUsername());
        return postService.deletePost(postId, user);
    }
}