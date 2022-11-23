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

import java.util.List;
import java.util.Map;

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
        FoodAllergyInfo foodAllergyInfo = foodAllergyRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품 알레르기 테이블이 존재하지 않습니다."));
        foodAllergyRepository.delete(foodAllergyInfo);
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        foodRepository.delete(food);
        return food.getFoodId();
    }

    @Transactional
    public Long updateFoodAllergy(Long foodId, AllergyInfo allergyInfo){
        FoodAllergyInfo foodAllergyInfo = foodAllergyRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품 알레르기 테이블이 존재하지 않습니다."));
        return foodAllergyRepository.save(foodAllergyInfo.updateFoodAllergy(allergyInfo)).getFoodId();
    }

    @Transactional(readOnly = true)
    public List<FoodListResponseDto> getViewFoodList(List<Long> foodList) {
        return foodRepository.findViewFoodsByFoodId(foodList);
    }

    @Transactional(readOnly = true)
    public Map<String, Boolean> getFoodAllergies(Long foodId){
        AllergyInfoDto foodAllergyInfo = foodRepository.findAllergyDtoByFoodId(foodId);
        return foodAllergyInfo.toMap();
    }
}
