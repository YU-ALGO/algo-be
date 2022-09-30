package com.stock.yu.downbitbe.board.domain.comment;

import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
public class CommentCreateRequestDto {
    @NotBlank
    private String content;
    private Long parent;

    public Comment toEntity(Post post, User user){
        return Comment.builder()
                .content(content)
                .user(user)
                .post(post)
                .parent(parent)
                .build();
    }
}