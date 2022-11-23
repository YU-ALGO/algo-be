package com.algo.yu.algobe.food.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class FoodRecommendIdListDto {
    @JsonProperty("item_id")
    private List<Long> recommendList;
}
