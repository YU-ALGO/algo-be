package com.stock.yu.downbitbe.board.domain.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    @JsonProperty("comment_count")
    private final Integer commentCount;
    @JsonProperty("like_count")
    private final Integer likeCount;
    @JsonProperty("created_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getNickname();
        this.commentCount = post.getCommentCount();
        this.likeCount = post.getLikeCount();
        this.createdAt = post.getCreatedAt();
    }
}
