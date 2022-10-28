package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.board.BoardRepository;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.board.domain.post.PostLike;
import com.stock.yu.downbitbe.board.domain.post.PostLikeRepository;
import com.stock.yu.downbitbe.board.domain.post.PostRepository;
import com.stock.yu.downbitbe.user.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class PostLikeService {
    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final BoardRepository boardRepository;

    @Transactional
    public Long createPostLike(Long boardId, Long postId, User user){
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        if(postLikeRepository.existsByPostPostIdAndUserUserId(postId, user.getUserId())){
            throw new RuntimeException("이미 추천한 게시글입니다.");
        }
        PostLike postLike = PostLike.builder().post(post).user(user).build();
        return postLikeRepository.save(postLike).getPostLikeId();
    }

    @Transactional
    public Long deletePostLike(Long boardId, Long postId, User user){
        boardRepository.findById(boardId).orElseThrow(() -> new IllegalArgumentException("게시판이 존재하지 않습니다."));
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        PostLike postLike = postLikeRepository.findPostLikeByPostPostIdAndUserUserId(postId, user.getUserId()).orElseThrow(() -> new IllegalArgumentException("추천한 게시물이 아닙니다."));
        postLikeRepository.delete(postLike);
        return postLike.getPostLikeId();
    }

    @Transactional
    public int updateLike(Long postId, User user, Integer symbol){
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return postRepository.updateLikeCount(postId, symbol);
    }

}
