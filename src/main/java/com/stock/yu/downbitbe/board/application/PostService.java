package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.*;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class PostService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public PostResponseDto findPostByPostId(Long boardId, Long postId){
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return new PostResponseDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findAllPostsById(Long boardId, Pageable pageable) {
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardId(boardId, pageable).stream()
                .map(PostListResponseDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long createPost(PostCreateRequestDto postCreateRequestDto, Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));

        Post post = postCreateRequestDto.toEntity(board, user);
        return postRepository.save(post).getPostId();
    }

    @Transactional
    public Long updatePost(PostUpdateRequestDto postUpdateRequestDto, Long boardId, Long postId, User user) {
        if(!postUpdateRequestDto.getPostId().equals(postId) || !postUpdateRequestDto.getBoardId().equals(boardId)){
            throw new RuntimeException("잘못된 요청입니다.");
        }
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        return postRepository.save(post.updatePost(postUpdateRequestDto.toEntity())).getPostId();
    }

    @Transactional
    public Long deletePost(Long postId, User user){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        postRepository.delete(post);
        return post.getPostId();
    }
}
