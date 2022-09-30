package com.stock.yu.downbitbe.board.domain.comment;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.user.entity.User;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "COMMENT")
@Getter
@DynamicInsert
@DynamicUpdate
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @Column(name = "comment_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @JoinColumn(name = "author")
    @ManyToOne
    @NotNull
    private User user;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Post post;

    @Column
    private Long parent;

    @Column(name = "is_deleted", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean isDeleted;

    @Builder
    private Comment(String content, User user, Post post, Long parent) {
        this.content = content;
        this.user = user;
        this.post = post;
        this.parent = parent;
    }

    public Comment updateComment(Comment comment) {
        if (comment.getContent() != null)
            this.content = comment.getContent();
        return this;
    }
}
