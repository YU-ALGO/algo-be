package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class PostCreateRequestDto {
    @NotBlank(message = "제목을 입력해주세요.")
    private final String title;
    @NotBlank(message = "내용을 입력해주세요")
    private final String content;

    public Post toEntity(Board board, User user) {
        return Post.builder()
                .title(title)
                .content(content)
                .board(board)
                .user(user)
                .build();
    }
}