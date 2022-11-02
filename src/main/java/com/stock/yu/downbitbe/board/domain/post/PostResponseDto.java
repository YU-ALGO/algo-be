package com.stock.yu.downbitbe.board.domain.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    @JsonProperty("comment_count")
    private final Integer commentCount;
    @JsonProperty("like_count")
    private final Integer likeCount;
    @JsonProperty("is_like")
    private final Boolean isLike;
    @JsonProperty("view_count")
    private final Long viewCount;

    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public PostResponseDto(Post post, Boolean isLike) {
        this.id = post.getPostId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getNickname();
        this.commentCount = post.getCommentCount();
        this.likeCount = post.getLikeCount();
        this.isLike = isLike;
        this.viewCount = post.getViewCount();
        this.createdAt = post.getCreatedAt();
    }
}
