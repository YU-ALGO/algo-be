package com.stock.yu.downbitbe.food.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.QMap;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.food.domain.FoodListResponseDto;
import com.stock.yu.downbitbe.food.domain.FoodRepositoryCustom;
import com.stock.yu.downbitbe.food.domain.QAllergyInfo;
import com.stock.yu.downbitbe.food.domain.QFoodAllergyInfo;
import com.stock.yu.downbitbe.user.domain.profile.UserFoodLikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.stock.yu.downbitbe.food.domain.QFood.food;
import static com.stock.yu.downbitbe.food.domain.QFoodAllergyInfo.foodAllergyInfo;
import static com.stock.yu.downbitbe.message.domain.QMessage.message;
import static org.springframework.util.StringUtils.hasText;

@Repository
@RequiredArgsConstructor
public class FoodRepositoryImpl implements FoodRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<FoodListResponseDto> findAllFoodsBy(Map<String, Boolean> allergyFilter, Pageable pageable, String keyword) {

        JPAQuery<FoodListResponseDto> query = queryFactory
                .select(Projections.constructor(FoodListResponseDto.class,
                        food.foodId,
                        food.foodImageUrl,
                        food.foodName,
                        food.likeCount
                ))
                .from(foodAllergyInfo)
                .innerJoin(foodAllergyInfo.food, food)
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



    BooleanExpression isSearchable(String keyword){
        if(!hasText(keyword))
            return null;
        return food.foodName.contains(keyword);
    }


}
