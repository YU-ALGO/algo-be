package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.*;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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

//    @Transactional(readOnly = true)
//    public List<PostListResponseDto> findAllPostsById(Long boardId, Pageable pageable) {
//        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
//        return postRepository.findAllByBoardId(boardId, pageable).stream()
//                .map(PostListResponseDto::new).collect(Collectors.toList());
//    }

    @Transactional(readOnly = true)
    public Page<Post> findAllPostsByBoardId(Long boardId, Pageable pageable) {
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardBoardId(boardId, pageable);
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
