package com.algo.yu.algobe.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodRepository extends JpaRepository<Food, Long>, FoodRepositoryCustom {
    @Modifying(clearAutomatically = true)
    @Query("update Food set likeCount = likeCount + :symbol where foodId = :foodId")
    int updateLikeCount(@Param(value = "foodId") Long foodId, @Param(value = "symbol") Integer symbol);

}
