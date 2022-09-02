package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.PostService;
import com.stock.yu.downbitbe.board.domain.post.PostCreateRequestDto;
import com.stock.yu.downbitbe.board.domain.post.PostResponseDto;
import com.stock.yu.downbitbe.board.domain.post.PostListResponseDto;
import com.stock.yu.downbitbe.board.domain.post.PostUpdateRequestDto;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
@Log4j2
public class PostController {
    private final PostService postService;

    @GetMapping("/{board_id}/posts")
    public List<PostListResponseDto> posts(@PathVariable("board_id") Long boardId) {
        return postService.findAllPostsById(boardId);
    }

    @GetMapping("/{board_id}/posts/{post_id}")
    public PostResponseDto post(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId) {
        return postService.findPostByPostId(postId);
    }

    @PostMapping("/{board_id}/posts")
    public Long createPost(@RequestBody PostCreateRequestDto postCreateRequestDto, @PathVariable("board_id") Long boardId){
        log.info("--------create-----");
        log.info("title" + postCreateRequestDto.getTitle());
        log.info("content" + postCreateRequestDto.getContent());

        //Long postId = postService.createPost(postCreateDto, boardId);
        User user = new User(); // 토큰 사용 후 사용자 정보 받기

        return postService.createPost(postCreateRequestDto, boardId, user);
    }

    @PatchMapping("/{board_id}/posts/{post_id}")
    public Long updatePost(@RequestBody PostUpdateRequestDto postUpdateRequestDto, @PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId){
        User user = new User();
        return postService.updatePost(postUpdateRequestDto, postId, user);
    }

    @DeleteMapping("/{board_id}/posts/{post_id}")
    public Long deletePost(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId){
        User user = new User();
        return postService.deletePost(postId, user);
    }
}
