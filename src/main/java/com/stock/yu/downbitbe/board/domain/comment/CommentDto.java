package com.stock.yu.downbitbe.board.domain.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class CommentDto {
    private Long id;
    private String content;
    private String author;

    public CommentDto(Comment comment){
        this.id = comment.getId();
        this.content = comment.getContent();
        this.author = comment.getUser().getNickname();
    }
}
