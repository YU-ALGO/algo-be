package com.algo.yu.algobe.food.infra;

import com.algo.yu.algobe.board.domain.post.Post;
import com.algo.yu.algobe.food.domain.*;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.springframework.util.StringUtils.hasText;
import static com.algo.yu.algobe.food.domain.QFood.food;
import static com.algo.yu.algobe.food.domain.QFoodAllergyInfo.foodAllergyInfo;

@Repository
@RequiredArgsConstructor
public class FoodRepositoryImpl implements FoodRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Food.class, "food");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
            orders.add(new OrderSpecifier(Order.DESC, food.foodId));

        });
        return orders;
    }

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
                .orderBy(getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .innerJoin(foodAllergyInfo.food, food)
                .where(isSearchable(keyword));

        allergyFilter.forEach((key, value) -> {
            PathBuilder<QAllergyInfo> entityPath = new
                    PathBuilder<>(QAllergyInfo.class, "foodAllergyInfo.allergyInfo");
            query.where(entityPath.getBoolean(key).isFalse());

        });

        int totalSize = query
                .fetch().size();

        List<FoodListResponseDto> results = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize()).fetch();

        //int totalSize = query.fetch().size();


        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public List<FoodListResponseDto> findViewFoodsByFoodId(List<Long> viewList) {
        return queryFactory
                .select(Projections.constructor(FoodListResponseDto.class,
                        food.foodId,
                        food.foodImageUrl,
                        food.foodName,
                        food.likeCount
                ))
                .from(food)
                .where(food.foodId.in(viewList))
                .fetch();
    }

    @Override
    public AllergyInfoDto findAllergyDtoByFoodId(Long foodId) {
        AllergyInfoDto allergyInfoDto = queryFactory
                .select(Projections.constructor(AllergyInfoDto.class, foodAllergyInfo.allergyInfo))
                .from(foodAllergyInfo)
                .where(foodAllergyInfo.foodId.eq(foodId))
                .fetchFirst();

        return allergyInfoDto;
    }

    BooleanExpression isSearchable(String keyword){
        if(!hasText(keyword))
            return null;
        return food.foodName.contains(keyword);
    }
}
