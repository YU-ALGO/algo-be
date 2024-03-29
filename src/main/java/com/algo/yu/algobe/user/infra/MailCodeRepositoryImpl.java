package com.algo.yu.algobe.user.infra;

import com.algo.yu.algobe.user.domain.user.MailCodeRepositoryCustom;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.algo.yu.algobe.user.domain.user.QMailCode.mailCode;

@Repository
@RequiredArgsConstructor
public class MailCodeRepositoryImpl implements MailCodeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Boolean findIsValidateByUsername(String username) {
        return queryFactory.select(mailCode.isValidate)
                .from(mailCode)
                .where(mailCode.username.eq(username))
                .fetchFirst();
    }
}
