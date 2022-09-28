package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.comment.*;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.board.domain.post.PostRepository;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    @Transactional(readOnly = true)
    public Page<Comment> findAllCommentsByPostId(Long postId, Pageable pageable){
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return commentRepository.findAllByPostPostId(postId, pageable);
    }

    @Transactional
    public Long createComment(CommentCreateRequestDto commentCreateRequestDto, Long postId, User user){
        Post post = postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = commentCreateRequestDto.toEntity(post, user);
        Long createCommentId = commentRepository.save(comment).getCommentId();
        return createCommentId;
    }

    @Transactional
    public int updateCommentCount(Long postId, User user, Integer symbol){
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        return postRepository.updateCommentCount(postId, symbol);
    }

//    @Transactional
//    public Long updateComment(CommentUpdateRequestDto commentUpdateRequestDto, Long postId, User user){
//    }

    //TODO : 1. 답글이 없을때 댓글 삭제하면 댓글 완전히 삭제  2. 답글이 존재할때 댓글 삭제하면 댓글 내용만 삭제  3. 답글 삭제할때 남은 답글이 삭제할 답글이 유일하고 댓글이 삭제된거면 다 같이 삭제
    @Transactional
    public Long deleteComment(Long postId, Long commentId, User user){
        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
        Comment comment = commentRepository.findById(commentId).orElseThrow(() -> new IllegalArgumentException("댓글이 존재하지 않습니다."));
        if(!comment.getUser().getUserId().equals(user.getUserId())){
            throw new RuntimeException("작성자와 일치하지 않습니다.");
        }
        commentRepository.delete(comment);
        return comment.getCommentId();
    }
}
