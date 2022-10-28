package com.stock.yu.downbitbe.message.domain;

import com.querydsl.jpa.impl.JPAQueryFactory;

import javax.persistence.EntityManager;

public class MessageRepositoryImpl implements MessageRepositoryCustom{
    private final JPAQueryFactory queryFactory;


    public MessageRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Long countNonReadMessageByUserId(Long userId) {
        return null;
    }
}
