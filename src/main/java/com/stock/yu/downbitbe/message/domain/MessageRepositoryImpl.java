package com.stock.yu.downbitbe.message.domain;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

import static com.stock.yu.downbitbe.message.domain.QMessage.message;
import static com.stock.yu.downbitbe.message.domain.QMessageBlock.messageBlock;


public class MessageRepositoryImpl implements MessageRepositoryCustom{
    private final JPAQueryFactory queryFactory;

    public MessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Integer countNonReadMessageByUserId(Long userId) {
        return queryFactory
                .selectFrom(message)
                .where(message.readTime.isNull()
                        .and(message.sender.notIn(
                                new JPQLQuery[]{JPAExpressions
                                        .selectFrom(messageBlock)
                                        .where(messageBlock.userId.userId.eq(userId))})))
                .fetch().size();
    }

    @Override
    public Long updateDeleteCondition(Long messageId, DeleteCondition condition) {
        return queryFactory
                .update(message)
                .set(message.deleted, condition)
                .where(message.messageId.eq(messageId))
                .execute();
    }
}
