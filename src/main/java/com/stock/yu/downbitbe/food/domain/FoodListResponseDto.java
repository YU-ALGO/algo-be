package com.stock.yu.downbitbe.food.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FoodListResponseDto {
    private Long id;
    @JsonProperty("food_image_url")
    private String foodImageUrl;
    @JsonProperty("food_name")
    private String foodName;
    @JsonProperty("like_count")
    private Integer likeCount;

    @QueryProjection
    public FoodListResponseDto(Long id, String foodImageUrl, String foodName, Integer likeCount) {
        this.id = id;
        this.foodImageUrl = foodImageUrl;
        this.foodName = foodName;
        this.likeCount = likeCount;
    }
}
