package com.stock.yu.downbitbe.user.domain.profile;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import com.stock.yu.downbitbe.food.domain.Food;
import com.stock.yu.downbitbe.food.domain.FoodListResponseDto;
import com.stock.yu.downbitbe.food.domain.FoodResponseDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Getter
@AllArgsConstructor
public class UserFoodLikeResponseDto {
    private Long id;
    private String foodName;
}
