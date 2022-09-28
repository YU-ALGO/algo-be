package com.stock.yu.downbitbe.board.domain.comment;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@RequiredArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank(message = "내용을 입력해주세요")
    private final String content;

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .build();
    }
}
