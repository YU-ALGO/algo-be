package com.stock.yu.downbitbe.food.application;

import com.stock.yu.downbitbe.food.domain.*;
import com.stock.yu.downbitbe.food.utils.AllergyUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Log4j2
@RequiredArgsConstructor
@Service
public class FoodService {
    private final FoodRepository foodRepository;
    private final FoodLikeRepository foodLikeRepository;
    private final FoodAllergyInfoRepository foodAllergyRepository;

    @Transactional(readOnly = true)
    public Page<FoodListResponseDto> findAllFoods(AllergyInfoDto allergyInfo, Pageable pageable, String keyword){
        return foodRepository.findAllFoodsBy(allergyInfo.toTrueMap(), pageable, keyword);
    }

    @Transactional(readOnly = true)
    public FoodResponseDto findFoodLikeByFoodId(Long foodId, Long userId){
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        Boolean isLike = foodLikeRepository.existsByFoodFoodIdAndUserUserId(foodId, userId);

        AllergyInfoDto result = foodAllergyRepository.findAllergyInfoById(food.getFoodId());
        log.info("food : " + AllergyUtils.mapToString(result.toTrueMap()));

        return new FoodResponseDto(food, isLike, AllergyUtils.mapToString(result.toTrueMap()));
    }

    @Transactional(readOnly = true)
    public Food findFoodByFoodId(Long foodId) {
        return foodRepository.findById(foodId).get();
    }

    public Long createFood(FoodRequestDto foodCreateRequestDto){
        Food food = foodCreateRequestDto.toEntity();
        return foodRepository.save(food).getFoodId();
    }

    public Long createFoodAllergy(Food food, AllergyInfo allergyInfo){
        FoodAllergyInfo foodAllergyInfo =
                FoodAllergyInfo.builder()
                        .food(food)
                        .allergyInfo(allergyInfo)
                        .build();
        return foodAllergyRepository.save(foodAllergyInfo).getFoodId();
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
