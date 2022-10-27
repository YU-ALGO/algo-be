package com.stock.yu.downbitbe.food.domain;

import lombok.Builder;
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
    @Column
    private String nutrition;

    @Column(name = "like_count")
    private Integer likeCount;

    //TODO : 사진 여러개..?
    @Column(name = "food_image_url")
    private String foodImageUrl;

    @Builder
    public Food(String foodName, Long code, String nutrition, String foodImageUrl){
        this.foodName = foodName;
        this.code = code;
        this.nutrition = nutrition;
        this.foodImageUrl = foodImageUrl;
    }


    public Food updateFood(Food food) {
        if(food.getFoodName() != null)
            this.foodName = food.getFoodName();
        if(food.getCode() != null)
            this.code = food.getCode();
        if(food.getNutrition() != null)
            this.nutrition = food.getNutrition();
        if(food.getFoodImageUrl() != null)
            this.foodImageUrl = food.getFoodImageUrl();
        return this;
    }
}
