package com.algo.yu.algobe.food.infra;

import com.algo.yu.algobe.food.domain.QFood;
import com.algo.yu.algobe.food.domain.QFoodLike;
import com.algo.yu.algobe.user.domain.profile.UserFoodLikeResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.algo.yu.algobe.food.domain.FoodLikeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.algo.yu.algobe.user.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class FoodLikeRepositoryImpl implements FoodLikeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<UserFoodLikeResponseDto> getUserFoodLikeListByNickname(String nickname, Pageable pageable) {
        List<UserFoodLikeResponseDto> results = queryFactory
                .select(Projections.constructor(UserFoodLikeResponseDto.class,
                        QFoodLike.foodLike.food.foodId,
                        QFoodLike.foodLike.food.foodName,
                        QFoodLike.foodLike.food.foodImageUrl))
                .from(QFoodLike.foodLike)
                .innerJoin(QFoodLike.foodLike.food, QFood.food)
                .innerJoin(QFoodLike.foodLike.user, user)
                .where(QFoodLike.foodLike.user.nickname.eq(nickname))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(QFoodLike.foodLike)
                .where(QFoodLike.foodLike.user.nickname.eq(nickname))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public List<Long> getFoodIdListByUsername(String username) {
        return queryFactory.select(QFoodLike.foodLike.food.foodId)
                .from(QFoodLike.foodLike)
                .innerJoin(QFoodLike.foodLike.user, user)
                .where(QFoodLike.foodLike.user.username.eq(username))
                .fetch();
    }


}
