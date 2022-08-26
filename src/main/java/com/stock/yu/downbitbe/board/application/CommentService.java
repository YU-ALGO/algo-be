package com.stock.yu.downbitbe.board.application;

import com.stock.yu.downbitbe.board.domain.comment.CommentDto;
import com.stock.yu.downbitbe.board.domain.comment.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;

    @Transactional(readOnly = true)
    public List<CommentDto> findAllComments(Long postId){
        return commentRepository.findAllByPostId(postId).stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
}
