package com.algo.yu.algobe.board.domain.post;

import com.algo.yu.algobe.board.domain.board.Board;
import com.algo.yu.algobe.user.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
