package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.user.entity.User;

import java.time.LocalDateTime;

public class PostResponseDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String author;
    private final Integer commentCount;
    private final Integer likeCount;
    private final LocalDateTime created_at;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.author = post.getUser().getNickname();
        this.commentCount = post.getComment();
        this.likeCount = post.getLike();
        this.created_at = post.getCreatedAt();
    }
}
