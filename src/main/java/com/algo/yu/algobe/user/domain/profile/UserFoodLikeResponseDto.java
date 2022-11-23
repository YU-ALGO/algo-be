package com.algo.yu.algobe.user.domain.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserFoodLikeResponseDto {
    private Long id;
    private String foodName;
    @JsonProperty("food_image_url")
    private String foodImageUrl;
}
