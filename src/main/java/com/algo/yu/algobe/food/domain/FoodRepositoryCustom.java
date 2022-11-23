package com.algo.yu.algobe.food.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface FoodRepositoryCustom {
    Page<FoodListResponseDto> findAllFoodsBy(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword);
    //Page<FoodListResponseDto> findAll(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword);
    List<FoodListResponseDto> findRecommendFoodsByFoodId(List<Long> recommendList);

    List<FoodListResponseDto> findViewFoodsByFoodId(List<Long> viewList);

    AllergyInfoDto findAllergyDtoByFoodId(Long foodId);
}
