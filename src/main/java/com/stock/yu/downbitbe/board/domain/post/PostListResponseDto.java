package com.stock.yu.downbitbe.board.domain.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListResponseDto {
    private Long id;
    private String title;
    private String author;
    @JsonProperty("like_count")
    private Integer likeCount;

    @JsonProperty("comment_count")
    private Integer commentCount;

    @JsonProperty("view_count")
    private Long viewCount;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    @QueryProjection
    public PostListResponseDto(Long postId, String title, String nickname, Integer likeCount, Integer commentCount, Long viewCount, LocalDateTime createdAt) {
        this.id = postId;
        this.title = title;
        this.author = nickname;
        this.likeCount = likeCount;
        this.commentCount = commentCount;
        this.viewCount = viewCount;
        this.createdAt = createdAt;
    }
}
