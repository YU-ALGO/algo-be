package com.stock.yu.downbitbe.food.domain;

import com.stock.yu.downbitbe.message.domain.DeleteCondition;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface FoodRepository extends JpaRepository<Food, Long>, FoodRepositoryCustom {
    @Modifying(clearAutomatically = true)
    @Query("update Food set likeCount = likeCount + :symbol where foodId = :foodId")
    int updateLikeCount(@Param(value = "foodId") Long foodId, @Param(value = "symbol") Integer symbol);

}
