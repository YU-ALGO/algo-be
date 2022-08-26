package com.stock.yu.downbitbe.board.ui;

import com.stock.yu.downbitbe.board.application.PostService;
import com.stock.yu.downbitbe.board.domain.post.PostCreateDto;
import com.stock.yu.downbitbe.board.domain.post.PostDto;
import com.stock.yu.downbitbe.board.domain.post.PostListDto;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/boards")
public class PostController {
    private final PostService postService;

    @GetMapping("/{board_id}/posts")
    public List<PostListDto> posts(@PathVariable("board_id") Long boardId) {
        return postService.findAllPostsById(boardId);
    }

    @GetMapping("/{board_id}/posts/{post_id}")
    public PostDto post(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId) {
        return postService.findPostByPostId(postId);
    }

    @PostMapping("/{board_id}/posts")
    public Long createPost(@RequestBody PostCreateDto postCreateDto, @PathVariable("board_id") Long boardId){
        return postService.createPost(postCreateDto, boardId);
    }

//    @PatchMapping("/{board_id}/posts/{post_id}")
//    public Long updatePost(@RequestBody PostDto postDto, @PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId){
//        return postService.updatePost(postDto, postId);
//    }

    @DeleteMapping("/{board_id}/posts/{post_id}")
    public Long deletePost(@PathVariable("board_id") Long boardId, @PathVariable("post_id") Long postId){
        return postService.deletePost(postId);
    }
}
