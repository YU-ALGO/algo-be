package com.stock.yu.downbitbe.food.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.food.domain.FoodListResponseDto;
import com.stock.yu.downbitbe.food.domain.FoodRepositoryCustom;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.stock.yu.downbitbe.food.domain.QFood.food;
import static com.stock.yu.downbitbe.food.domain.QFoodAllergyInfo.foodAllergyInfo;
import static com.stock.yu.downbitbe.message.domain.QMessage.message;
import static org.springframework.util.StringUtils.hasText;

public class FoodRepositoryImpl implements FoodRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public FoodRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<FoodListResponseDto> findAllFoodsBy(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword) {
        List<FoodListResponseDto> results = queryFactory
                .select(Projections.constructor(FoodListResponseDto.class,
                        food.foodId,
                        food.foodName,
                        food.likeCount
                ))
                .from(food)
                .innerJoin(food, foodAllergyInfo.food)
                .where(isSearchable(keyword))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(food)
                .where(isSearchable(keyword))
                .fetch().size();


        return new PageImpl<>(results, pageable, totalSize);
    }

    BooleanExpression isSearchable(String keyword){
        if(!hasText(keyword))
            return null;
        return food.foodName.contains(keyword);
    }


}
