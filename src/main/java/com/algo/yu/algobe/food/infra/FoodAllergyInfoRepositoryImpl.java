package com.algo.yu.algobe.food.infra;

import com.algo.yu.algobe.food.domain.FoodAllergyInfoRepositoryCustom;
import com.algo.yu.algobe.food.domain.QFood;
import com.algo.yu.algobe.food.domain.QFoodAllergyInfo;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.algo.yu.algobe.food.domain.AllergyInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class FoodAllergyInfoRepositoryImpl implements FoodAllergyInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public AllergyInfoDto findAllergyInfoById(Long id) {
        AllergyInfoDto allergyInfoDto = queryFactory
                .select(Projections.constructor(AllergyInfoDto.class, QFoodAllergyInfo.foodAllergyInfo.allergyInfo))
                .from(QFoodAllergyInfo.foodAllergyInfo)
                .innerJoin(QFoodAllergyInfo.foodAllergyInfo.food, QFood.food)
                .where(QFoodAllergyInfo.foodAllergyInfo.foodId.eq(id))
                .fetchFirst();
        return allergyInfoDto;
    }
}
