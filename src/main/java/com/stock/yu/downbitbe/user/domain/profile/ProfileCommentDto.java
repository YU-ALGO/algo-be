package com.stock.yu.downbitbe.user.domain.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.yu.downbitbe.board.domain.comment.Comment;

import java.time.LocalDateTime;

public class ProfileCommentDto {
    private final Long boardId;
    private final Long postId;
    private final String content;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public ProfileCommentDto(Comment comment, Long boardId, Long postId) {
        this.content = comment.getContent();
        this.boardId = boardId;
        this.postId = postId;
        this.createdAt = comment.getCreatedAt();
    }
}
