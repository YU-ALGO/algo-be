package com.stock.yu.downbitbe.food.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.food.domain.FoodLikeRepositoryCustom;
import com.stock.yu.downbitbe.user.domain.profile.UserFoodLikeResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.stock.yu.downbitbe.food.domain.QFood.food;
import static com.stock.yu.downbitbe.food.domain.QFoodLike.foodLike;
import static com.stock.yu.downbitbe.user.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class FoodLikeRepositoryImpl implements FoodLikeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public List<UserFoodLikeResponseDto> getUserFoodLikeListByNickname(String nickname) {
        return queryFactory.select(Projections.constructor(UserFoodLikeResponseDto.class,
                        foodLike.food.foodId,
                        foodLike.food.foodName))
                        .from(foodLike)
                        .innerJoin(foodLike.food, food)
                        .innerJoin(foodLike.user, user)
                        .where(foodLike.user.nickname.eq(nickname))
                        .fetch();
    }

    @Override
    public List<Long> getFoodIdListByUsername(String username) {
        return queryFactory.select(foodLike.food.foodId)
                .from(foodLike)
                .innerJoin(foodLike.user, user)
                .where(foodLike.user.username.eq(username))
                .fetch();
    }


}
