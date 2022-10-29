package com.stock.yu.downbitbe.user.domain.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.yu.downbitbe.board.domain.comment.Comment;
import com.stock.yu.downbitbe.board.domain.post.Post;

import java.time.LocalDateTime;

public class ProfilePostDto {
    private final Long boardId;
    private final Long postId;
    private final String title;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public ProfilePostDto(Post post, Long boardId, Long postId) {
        this.title = post.getContent();
        this.postId = postId;
        this.boardId = boardId;
        this.createdAt = post.getCreatedAt();
    }
}
