package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.*;
import com.stock.yu.downbitbe.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.LocalDate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
@Slf4j
public class PostService {
    private final BoardRepository boardRepository;
    private final PostRepository postRepository;
    private final PostLikeRepository postLikeRepository;

    @Transactional(readOnly = true)
    public PostResponseDto findPostByPostId(Long boardId, Long postId, Long userId){
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Boolean isLike = postLikeRepository.existsByPostPostIdAndUserUserId(postId, userId);
        return new PostResponseDto(post, isLike);
    }

    @Transactional(readOnly = true)
    public Page<PostListResponseDto> findAllPostsByBoardId(Long boardId, Pageable pageable, String keyword, PostSearchType searchType) {
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardBoardId(boardId, pageable, keyword, searchType);
    }

    //TODO : 캐시 사용하여 중복 방지 구현
    @Transactional
    public int updateView(Long postId, User user){
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return postRepository.updateViewCount(postId);
    }

    @Transactional
    public Long createPost(PostCreateRequestDto postCreateRequestDto, Long boardId, User user) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));

        Post post = postCreateRequestDto.toEntity(board, user);
        return postRepository.save(post).getPostId();
    }

    @Transactional
    public Long updatePost(PostUpdateRequestDto postUpdateRequestDto, Long boardId, Long postId, User user) {
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        if(!post.getBoard().getBoardId().equals(boardId)) {
            throw new RuntimeException("게시판과 게시글이 일치하지 않습니다.");
        }
        return postRepository.save(post.updatePost(postUpdateRequestDto.toEntity())).getPostId();
    }

    @Transactional
    public Long deletePost(Long boardId, Long postId, User user){
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(!post.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        if(!post.getBoard().getBoardId().equals(boardId)) {
            throw new RuntimeException("게시판과 게시글이 일치하지 않습니다.");
        }
        postRepository.delete(post);
        return post.getPostId();
    }

    @Transactional(readOnly = true)
    public List<PostListResponseDto> findTopPosts(int size) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime start = now.minusDays(1);
        return postRepository.findTopPosts(size, start, now);
    }
}
