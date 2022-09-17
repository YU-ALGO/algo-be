package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@NoArgsConstructor
public class PostImage extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Long id;

    @JoinColumn(name = "post_id")
    @ManyToOne
    private Post postId;

    @Column(name = "image_path")
    @NotNull
    private String path;


    @Builder
    public PostImage(String path){
        this.path = path;
    }
}
