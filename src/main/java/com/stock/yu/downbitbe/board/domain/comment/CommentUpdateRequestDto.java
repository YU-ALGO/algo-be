package com.stock.yu.downbitbe.board.domain.comment;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CommentUpdateRequestDto {
    @NotBlank(message = "내용을 입력해주세요")
    private String content;

    public Comment toEntity(){
        return Comment.builder()
                .content(content)
                .build();
    }
}
