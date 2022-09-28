package com.stock.yu.downbitbe.food.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "FOOD")
@Getter
@NoArgsConstructor
public class Food {
    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @Column(name = "food_name", length = 30)
    private String foodName;

    @Column
    private Long code;

    // TODO : 영양성분표

    @Column(name = "like_count")
    private Integer likeCount;

    //TODO : 사진 여러개..?
    @Column(name = "food_image_url")
    private String foodImageUrl;
}
