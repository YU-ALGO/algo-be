package com.stock.yu.downbitbe.food.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Map;

public interface FoodRepositoryCustom {
    Page<FoodListResponseDto> findAll(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword);
}
