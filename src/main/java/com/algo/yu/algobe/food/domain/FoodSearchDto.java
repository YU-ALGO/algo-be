package com.algo.yu.algobe.food.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class FoodSearchDto {
    String keyword;
    AllergyInfoDto allergyFilter;
}
