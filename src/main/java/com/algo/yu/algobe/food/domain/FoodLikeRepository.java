package com.algo.yu.algobe.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FoodLikeRepository extends JpaRepository<FoodLike, Long>, FoodLikeRepositoryCustom {
    Boolean existsByFoodFoodIdAndUserUserId(Long foodId, Long userId);

    Boolean existsByFood_FoodIdAndUser_Username(Long foodId, String username);

    Optional<FoodLike> findFoodLikeByFoodFoodIdAndUserUserId(Long foodId, Long userId);
}
