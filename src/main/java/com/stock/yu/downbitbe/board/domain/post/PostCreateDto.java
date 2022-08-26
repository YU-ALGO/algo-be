package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.user.entity.User;

public class PostCreateDto{
    private String title;
    private String content;

    public Post toEntity(Board board, User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .board(board)
                .user(user)
                .build();
    }
}
