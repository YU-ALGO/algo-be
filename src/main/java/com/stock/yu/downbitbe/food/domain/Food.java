package com.stock.yu.downbitbe.food.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FOOD")
@Getter
@NoArgsConstructor
public class Food {
    @Id
    @Column(name = "food_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodId;

    @Column(name = "food_name", length = 50)
    private String foodName;

    @Column
    private Long code;

    // TODO : 영양성분표
    @Column(columnDefinition = "TEXT")
    private String nutrition;

    @Column(columnDefinition = "TEXT")
    private String rawMaterials;

    @Column
    private String productKind;

    @Column(name = "like_count")
    @NotNull
    @ColumnDefault("0")
    private Integer likeCount;

    //TODO : 사진 여러개..?
    @Column(name = "food_image_url")
    private String foodImageUrl;

    @Builder
    public Food(String foodName, Long code, String nutrition, String rawMaterials, String productKind, String foodImageUrl){
        this.foodName = foodName;
        this.code = code;
        this.nutrition = nutrition;
        this.rawMaterials = rawMaterials;
        this.productKind = productKind;
        this.foodImageUrl = foodImageUrl;
    }

    public Food updateFood(Food food) {
        if(food.getFoodName() != null)
            this.foodName = food.getFoodName();
        if(food.getCode() != null)
            this.code = food.getCode();
        if(food.getNutrition() != null)
            this.nutrition = food.getNutrition();
        if(food.getRawMaterials() != null)
            this.rawMaterials = food.getRawMaterials();
        if(food.getProductKind() != null)
            this.productKind = food.getProductKind();
        if(food.getFoodImageUrl() != null)
            this.foodImageUrl = food.getFoodImageUrl();
        return this;
    }
}
