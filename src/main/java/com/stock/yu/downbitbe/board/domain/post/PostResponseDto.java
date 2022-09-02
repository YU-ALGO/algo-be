package com.stock.yu.downbitbe.board.domain.post;

import java.time.LocalDateTime;

public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String author;
    private Integer commentCount;
    private Integer likeCount;
    private LocalDateTime created_at;

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
