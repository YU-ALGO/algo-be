package com.algo.yu.algobe.food.infra;

import com.algo.yu.algobe.food.domain.*;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class FoodRepositoryImpl implements FoodRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FoodListResponseDto> findAllFoodsBy(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword) {

        JPAQuery<FoodListResponseDto> query = queryFactory
                .select(Projections.constructor(FoodListResponseDto.class,
                        QFood.food.foodId,
                        QFood.food.foodImageUrl,
                        QFood.food.foodName,
                        QFood.food.likeCount
                ))
                .from(QFoodAllergyInfo.foodAllergyInfo)
                .innerJoin(QFoodAllergyInfo.foodAllergyInfo.food, QFood.food)
                .where(isSearchable(keyword));

        allergyFilter.forEach((key, value) -> {
            PathBuilder<QAllergyInfo> entityPath = new
                    PathBuilder<>(QAllergyInfo.class, "foodAllergyInfo.allergyInfo");
            query.where(entityPath.getBoolean(key).isFalse());

        });

        int totalSize = query
                .fetch().size();

        List<FoodListResponseDto> results = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();

        //int totalSize = query.fetch().size();


        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public List<FoodListResponseDto> findRecommendFoodsByFoodId(List<Long> recommendList) {

        return queryFactory
                .select(Projections.constructor(FoodListResponseDto.class,
                        QFood.food.foodId,
                        QFood.food.foodImageUrl,
                        QFood.food.foodName,
                        QFood.food.likeCount
                ))
                .from(QFood.food)
                .where(QFood.food.foodId.in(recommendList))
                .fetch();
    }

    @Override
    public List<FoodListResponseDto> findViewFoodsByFoodId(List<Long> viewList) {
        return queryFactory
                .select(Projections.constructor(FoodListResponseDto.class,
                        QFood.food.foodId,
                        QFood.food.foodImageUrl,
                        QFood.food.foodName,
                        QFood.food.likeCount
                ))
                .from(QFood.food)
                .where(QFood.food.foodId.in(viewList))
                .fetch();
    }

    @Override
    public AllergyInfoDto findAllergyDtoByFoodId(Long foodId) {
        AllergyInfoDto allergyInfoDto = queryFactory
                .select(Projections.constructor(AllergyInfoDto.class, QFoodAllergyInfo.foodAllergyInfo.allergyInfo))
                .from(QFoodAllergyInfo.foodAllergyInfo)
                .where(QFoodAllergyInfo.foodAllergyInfo.foodId.eq(foodId))
                .fetchFirst();

        return allergyInfoDto;
    }

    BooleanExpression isSearchable(String keyword){
        if(!hasText(keyword))
            return null;
        return QFood.food.foodName.contains(keyword);
    }
}
