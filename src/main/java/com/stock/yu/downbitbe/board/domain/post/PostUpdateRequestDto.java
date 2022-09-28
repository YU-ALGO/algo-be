package com.stock.yu.downbitbe.board.domain.post;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
public class PostUpdateRequestDto {
    @JsonProperty("board_id")
    private Long boardId;

    @JsonProperty("post_id")
    private Long postId;

    @NotBlank
    private String title;
    @NotBlank
    private String content;

    public Post toEntity() {
        return Post.builder()
                .title(title)
                .content(content)
                .build();
    }
}