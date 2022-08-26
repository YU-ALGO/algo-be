package com.stock.yu.downbitbe.post.entity;

import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "LIKES") // like 는 mysql 예약어
@Getter
@NoArgsConstructor
public class Like {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id")
    @ManyToOne
    @NotNull
    private Post post;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @NotNull
    private User user;

    @Builder
    public Like(Post post, User user){
        this.post = post;
        this.user = user;
    }
}