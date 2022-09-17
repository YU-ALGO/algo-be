package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.comment.CommentDto;
import com.stock.yu.downbitbe.board.domain.comment.CommentRepository;
import com.stock.yu.downbitbe.board.domain.post.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
//    @Transactional(readOnly = true)
//    public List<CommentDto> findAllComments(Long postId){
//        postRepository.findById(postId).orElseThrow(() -> new IllegalArgumentException("게시글이 존재하지 않습니다."));
//        return commentRepository.findAllByPostId(postId).stream()
//                .map(CommentDto::new)
//                .collect(Collectors.toList());
//    }

//    @Transactional
//    public Long createComment(){
//
//    }
}
