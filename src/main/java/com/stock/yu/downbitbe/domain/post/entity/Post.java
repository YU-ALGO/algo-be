package com.stock.yu.downbitbe.domain.post.entity;

import com.stock.yu.downbitbe.domain.BaseTimeEntity;
import com.stock.yu.downbitbe.domain.board.entity.Board;
import com.stock.yu.downbitbe.domain.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "POST")
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    @NotNull
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotNull
    private String content;

    @JoinColumn(name = "author")
    @ManyToOne(cascade = CascadeType.ALL) // 구글링 한번 더 해서 공부
    @NotNull
    private User user;

    @JoinColumn(name = "board_id")
    @ManyToOne(cascade = CascadeType.ALL)
    @NotNull
    private Board board;

    @Column(name = "like_count")
    private Integer like;

    @Column(name = "comment_count")
    private Integer comment;

    @Builder
    public Post(String title, String content, User user, Board board, Integer like, Integer comment){
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;
        this.like = like;
        this.comment = comment;
    }

}
