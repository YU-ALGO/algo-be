package com.stock.yu.downbitbe.domain.post.entity;

import com.stock.yu.downbitbe.domain.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "LIKE")
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
