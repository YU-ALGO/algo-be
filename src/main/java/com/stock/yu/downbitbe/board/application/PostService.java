package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.*;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
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
    public PostDto findPostByPostId(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return new PostDto(post);
    }

    @Transactional(readOnly = true)
    public List<PostListDto> findAllPostsById(Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        return postRepository.findAllByBoardId(boardId).stream()
                .map(PostListDto::new).collect(Collectors.toList());
    }

    @Transactional
    public Long createPost(PostCreateDto postCreateDto, Long boardId) {
        Board board = boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        User user = new User(); // 토큰 사용 후 사용자 정보 받기
        Post post = postCreateDto.toEntity(board, user);
        return postRepository.save(post).getId();
    }

//    @Transactional
//    public Long updatePost(PostDto postDto, Long postId) {
//        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
//        User user = new User();     // 사용자 정보 확인 후 작성자만 수정 가능
//
//    }

    @Transactional
    public Long deletePost(Long postId){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        User user = new User(); // 사용자 정보 확인 후 작성자 or admin 삭제 가능
        postRepository.delete(post);
        return post.getId();
    }


}
