package com.stock.yu.downbitbe.domain.post.entity;

import com.stock.yu.downbitbe.domain.BaseTimeEntity;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "COMMENT")
@Getter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @JoinColumn(name = "author")
    @ManyToOne
    @NotNull
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @NotNull
    private Post post;

    @Builder
    private Comment(String content, User user, Post post){
        this.content = content;
        this.user = user;
        this.post = post;
    }
}
