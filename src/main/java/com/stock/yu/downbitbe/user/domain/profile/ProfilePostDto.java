package com.stock.yu.downbitbe.user.domain.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.stock.yu.downbitbe.board.domain.comment.Comment;
import com.stock.yu.downbitbe.board.domain.post.Post;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.stream.DoubleStream;

@Getter
@RequiredArgsConstructor
public class ProfilePostDto {
    private final Long boardId;
    private final Long postId;
    private final String title;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    @QueryProjection
    public ProfilePostDto(Post post, Long boardId, Long postId) {
        this.title = post.getTitle();
        this.postId = postId;
        this.boardId = boardId;
        this.createdAt = post.getCreatedAt();
    }
}
