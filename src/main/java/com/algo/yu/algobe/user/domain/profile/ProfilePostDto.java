package com.algo.yu.algobe.user.domain.profile;

import com.algo.yu.algobe.board.domain.post.Post;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

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
