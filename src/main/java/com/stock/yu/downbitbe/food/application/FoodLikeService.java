package com.stock.yu.downbitbe.food.application;

import com.stock.yu.downbitbe.food.domain.Food;
import com.stock.yu.downbitbe.food.domain.FoodLike;
import com.stock.yu.downbitbe.food.domain.FoodLikeRepository;
import com.stock.yu.downbitbe.food.domain.FoodRepository;
import com.stock.yu.downbitbe.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class FoodLikeService {
    private final FoodLikeRepository foodLikeRepository;
    private final FoodRepository foodRepository;

    public Long createFoodLike(Long foodId, User user) {
        Food food = foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        if(foodLikeRepository.existsByFoodFoodIdAndUserUserId(foodId, user.getUserId())){
            throw new RuntimeException("이미 즐겨찾기한 식품입니다.");
        }
        FoodLike foodLike = FoodLike.builder().food(food).user(user).build();
        return foodLikeRepository.save(foodLike).getFoodLikeId();
    }

    public Long deleteFoodLike(Long foodId, User user) {
        foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        FoodLike foodLike = foodLikeRepository.findFoodLikeByFoodFoodIdAndUserUserId(foodId, user.getUserId()).orElseThrow(() -> new IllegalArgumentException("추천한 게시물이 아닙니다."));
        foodLikeRepository.delete(foodLike);
        return foodLike.getFoodLikeId();
    }

    public int updateLike(Long foodId, User user, Integer symbol) {
        foodRepository.findById(foodId).orElseThrow(() -> new IllegalArgumentException("식품이 존재하지 않습니다."));
        return foodRepository.updateLikeCount(foodId, symbol);
    }

}
