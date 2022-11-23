package com.stock.yu.downbitbe.user.infra;

import com.querydsl.core.Tuple;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanPath;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import com.stock.yu.downbitbe.food.domain.FoodListResponseDto;
import com.stock.yu.downbitbe.food.domain.QAllergyInfo;
import com.stock.yu.downbitbe.user.domain.user.UserAllergyInfoRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.stock.yu.downbitbe.food.domain.QAllergyInfo.allergyInfo;
import static com.stock.yu.downbitbe.food.domain.QFood.food;
import static com.stock.yu.downbitbe.user.domain.user.QUserAllergyInfo.userAllergyInfo;
import static com.stock.yu.downbitbe.user.domain.user.QUser.user;

@Repository
@RequiredArgsConstructor
public class UserAllergyInfoRepositoryImpl implements UserAllergyInfoRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public AllergyInfoDto findAllergyDtoByUserId(Long userId) {
        AllergyInfoDto allergyInfoDto = queryFactory
                .select(Projections.constructor(AllergyInfoDto.class, userAllergyInfo.allergyInfo))
               .from(userAllergyInfo)
                .innerJoin(userAllergyInfo.user, user)
               .where(userAllergyInfo.userId.eq(userId))
               .fetchFirst();

        return allergyInfoDto;
    }
}
