package com.stock.yu.downbitbe.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodLikeRepository extends JpaRepository<FoodLike, Long> {
    Boolean existsByFoodFoodIdAndUserUserId(Long foodId, Long userId);

    Optional<FoodLike> findFoodLikeByFoodFoodIdAndUserUserId(Long foodId, Long userId);
}
