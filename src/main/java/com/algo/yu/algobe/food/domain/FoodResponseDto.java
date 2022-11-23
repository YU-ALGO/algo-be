package com.algo.yu.algobe.food.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class FoodResponseDto {
    private final Long id;
    @JsonProperty("food_name")
    private final String foodName;
    private final Long code;
    @JsonProperty("product_kind")
    private final String productKind;
    private final String nutrition;
    @JsonProperty("raw_materials")
    private final String rawMaterials;
    @JsonProperty("like_count")
    private final Integer likeCount;
    @JsonProperty("is_like")
    private final Boolean isLike;
    @JsonProperty("food_image_url")
    private final String foodImageUrl;

    @JsonProperty("allergy")
    private final String allergy;


    public FoodResponseDto(Food food, Boolean isLike, String allergy) {
        this.id = food.getFoodId();
        this.foodName = food.getFoodName();
        this.code = food.getCode();
        this.productKind = food.getProductKind();
        this.nutrition = food.getNutrition();
        this.rawMaterials = food.getRawMaterials();
        this.likeCount = food.getLikeCount();
        this.isLike = isLike;
        this.foodImageUrl = food.getFoodImageUrl();
        this.allergy = allergy;
    }
}
