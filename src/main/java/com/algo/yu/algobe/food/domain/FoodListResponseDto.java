package com.algo.yu.algobe.food.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @JsonProperty("is_like")
    private Boolean isLike;

    public void setIsLike(Boolean like) {
        isLike = like;
    }

    @QueryProjection
    public FoodListResponseDto(Long id, String foodImageUrl, String foodName, Integer likeCount) {
        this.id = id;
        this.foodImageUrl = foodImageUrl;
        this.foodName = foodName;
        this.likeCount = likeCount;
    }
}
