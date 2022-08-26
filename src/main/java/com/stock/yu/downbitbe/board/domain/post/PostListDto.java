package com.stock.yu.downbitbe.board.domain.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class PostListDto {
    private Long id;
    private String title;
    private String author;
    private LocalDateTime created_at;

    public PostListDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.author = post.getUser().getNickname();
        this.created_at = post.getCreatedAt();
    }
}
