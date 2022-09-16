package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;

@Entity
@Table(name = "POST")
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @JoinColumn(name = "author")
    @ManyToOne // 구글링 한번 더 해서 공부
    @NotNull
    private User user;

    @JoinColumn(name = "board_id")
    @ManyToOne
    @NotNull
    private Board board;

    @Column(name = "like_count")
    @NotNull
    @ColumnDefault("0")
    private Integer like;

    @Column(name = "comment_count")
    @NotNull
    @ColumnDefault("0")
    private Integer comment;

    //TODO 조회수 필드 추가

    @Builder
    public Post(String title, String content, User user, Board board, Integer like, Integer comment){
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;
        this.like = like;
        this.comment = comment;
    }

    public Post updatePost(Post post){
        if(post.getTitle() != null)
            this.title = post.getTitle();
        if(post.getContent() != null)
            this.content = post.getContent();
        return this;
    }
}
