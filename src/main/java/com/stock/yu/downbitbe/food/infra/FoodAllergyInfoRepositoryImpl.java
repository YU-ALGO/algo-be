package com.stock.yu.downbitbe.food.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import com.stock.yu.downbitbe.food.domain.FoodAllergyInfoRepositoryCustom;
import com.stock.yu.downbitbe.food.domain.FoodLikeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.stock.yu.downbitbe.food.domain.QFood.food;
import static com.stock.yu.downbitbe.food.domain.QFoodAllergyInfo.foodAllergyInfo;

@Repository
@RequiredArgsConstructor
public class FoodAllergyInfoRepositoryImpl implements FoodAllergyInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public AllergyInfoDto findAllergyInfoById(Long id) {
        AllergyInfoDto allergyInfoDto = queryFactory
                .select(Projections.constructor(AllergyInfoDto.class, foodAllergyInfo.allergyInfo))
                .from(foodAllergyInfo)
                .innerJoin(foodAllergyInfo.food, food)
                .where(foodAllergyInfo.foodId.eq(id))
                .fetchFirst();
        return allergyInfoDto;
    }
}
