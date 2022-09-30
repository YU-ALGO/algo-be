package com.stock.yu.downbitbe.board.domain.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {
    @NotBlank
    private String title;
    @NotBlank
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