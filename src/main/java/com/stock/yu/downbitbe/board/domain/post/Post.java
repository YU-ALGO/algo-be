package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.board.domain.board.Board;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.*;

import javax.persistence.*;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "POST")
@DynamicInsert
@DynamicUpdate
@Getter
@NoArgsConstructor
public class Post extends BaseTimeEntity {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @Column(length = 50)
    @NotBlank
    private String title;

    @Column(columnDefinition = "TEXT")
    @NotBlank
    private String content;

    @JoinColumn(name = "author")
    @ManyToOne
    @NotNull
    private User user;

    @JoinColumn(name = "board_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Board board;

    @Column(name = "like_count")
    @NotNull
    @ColumnDefault("0")
    private Integer likeCount;

    @Column(name = "comment_count")
    @NotNull
    @ColumnDefault("0")
    private Integer commentCount;

//    @OneToMany(fetch = FetchType.LAZY,
//            cascade = {CascadeType.PERSIST, CascadeType.REMOVE},
//            orphanRemoval = true
//    )

    @ElementCollection
    private List<String> postImg = new ArrayList<String>();

//    @ElementCollection
//    @Builder.Default
//    private Set<String> imageUrl = new HashSet<>();


    @Column(name = "view_count")
    @NotNull
    @ColumnDefault("0")
    private Long viewCount;

    @Builder
    public Post(String title, String content, User user, Board board){
        this.title = title;
        this.content = content;
        this.user = user;
        this.board = board;
    }

    public Post updatePost(Post post){
        if(post.getTitle() != null)
            this.title = post.getTitle();
        if(post.getContent() != null)
            this.content = post.getContent();
        return this;
    }

    public void addImage(String url){
        postImg.add(url);
    }
}