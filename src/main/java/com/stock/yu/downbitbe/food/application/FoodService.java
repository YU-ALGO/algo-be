package com.stock.yu.downbitbe.food.application;

import com.stock.yu.downbitbe.food.domain.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodLikeRepository foodLikeRepository;

    @Transactional(readOnly = true)
    public Page<FoodListResponseDto> findAllFoods(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword){
        return foodRepository.findAllFoodsBy(allergyFilter, pageable, keyword);
    }

    @Transactional(readOnly = true)
    public FoodResponseDto findFoodByFoodId(Long foodId, Long userId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        Boolean isLike = foodLikeRepository.existsByFoodFoodIdAndUserUserId(foodId, userId);
        return new FoodResponseDto(food, isLike);
    }

    @Transactional
    public Long createFood(FoodRequestDto foodCreateRequestDto){
        Food food = foodCreateRequestDto.toEntity();
        return foodRepository.save(food).getFoodId();
    }

    @Transactional
    public Long updateFood(FoodRequestDto foodRequestDto, Long foodId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        return foodRepository.save(food.updateFood(foodRequestDto.toEntity())).getFoodId();
    }

    @Transactional
    public Long deleteFood(Long foodId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        foodRepository.delete(food);
        return food.getFoodId();
    }
}
