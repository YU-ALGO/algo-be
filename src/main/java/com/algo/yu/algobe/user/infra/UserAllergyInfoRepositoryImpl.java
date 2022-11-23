package com.algo.yu.algobe.user.infra;

import com.algo.yu.algobe.user.domain.user.UserAllergyInfoRepositoryCustom;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.algo.yu.algobe.food.domain.AllergyInfoDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;


import static com.algo.yu.algobe.user.domain.user.QUserAllergyInfo.userAllergyInfo;
import static com.algo.yu.algobe.user.domain.user.QUser.user;

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
