package com.stock.yu.downbitbe.user.infra;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.user.domain.user.MailCodeRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import static com.stock.yu.downbitbe.user.domain.user.QMailCode.mailCode;

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
