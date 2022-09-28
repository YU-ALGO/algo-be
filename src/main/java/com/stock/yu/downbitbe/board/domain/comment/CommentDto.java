package com.stock.yu.downbitbe.board.domain.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
public class CommentDto {
    private Long id;
    private String content;
    private String author;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public CommentDto(Comment comment){
        this.id = comment.getCommentId();
        this.content = comment.getContent();
        this.author = comment.getUser().getNickname();
        this.createdAt = comment.getCreatedAt();
    }
}
