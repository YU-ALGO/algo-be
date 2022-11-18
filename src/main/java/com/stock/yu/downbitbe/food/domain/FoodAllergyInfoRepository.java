package com.stock.yu.downbitbe.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodAllergyInfoRepository extends JpaRepository<FoodAllergyInfo, Long>, FoodAllergyInfoRepositoryCustom {

}
