package com.stock.yu.downbitbe.food.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FoodResponseDto {
    private final Long id;
    @JsonProperty("food_name")
    private final String foodName;
    private final Long code;
    private final String nutrition;
    @JsonProperty("like_count")
    private final Integer likeCount;
    @JsonProperty("is_like")
    private final Boolean isLike;
    @JsonProperty("food_image_url")
    private final String foodImageUrl;


    public FoodResponseDto(Food food, Boolean isLike) {
        this.id = food.getFoodId();
        this.foodName = food.getFoodName();
        this.code = food.getCode();
        this.nutrition = food.getNutrition();
        this.likeCount = food.getLikeCount();
        this.isLike = isLike;
        this.foodImageUrl = food.getFoodImageUrl();
    }
}
