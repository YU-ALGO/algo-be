package com.algo.yu.algobe.board.domain.comment;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class CommentDto {
    private final Long id;
    private final String content;
    private final String author;
    private final Long parent;
    @JsonProperty("is_deleted")
    private final Boolean isDeleted;    // 닉네임 클릭하여 프로필 조회하는거 방지
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    @JsonProperty("modified_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime modifiedAt;

    public CommentDto(Comment comment){
        this.id = comment.getCommentId();
        this.content = comment.getContent();
        this.author = comment.getUser().getNickname();
        this.parent = comment.getParent();
        this.isDeleted = comment.getIsDeleted();
        this.createdAt = comment.getCreatedAt();
        this.modifiedAt = comment.getModifiedAt();
    }
}
