package com.algo.yu.algobe.food.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodAllergyInfoRepository extends JpaRepository<FoodAllergyInfo, Long>, FoodAllergyInfoRepositoryCustom {

}
