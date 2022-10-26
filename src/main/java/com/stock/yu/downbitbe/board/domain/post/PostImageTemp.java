package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.BaseTimeEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "POSTIMAGETEMP")
@NoArgsConstructor
public class PostImageTemp extends BaseTimeEntity {
    @Id
    @Column(name = "image_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long imageId;
    private String filename;

    public PostImageTemp(String filename){
        this.filename = filename;
    }
}
