package com.stock.yu.downbitbe.message.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.message.domain.DeleteCondition;
import com.stock.yu.downbitbe.message.domain.MessageRepositoryCustom;
import com.stock.yu.downbitbe.message.domain.ReceiveMessageListDto;
import com.stock.yu.downbitbe.message.domain.SendMessageListDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;

import java.util.List;

import static com.stock.yu.downbitbe.message.domain.QMessage.message;
import static com.stock.yu.downbitbe.user.domain.user.QUser.user;
import static com.stock.yu.downbitbe.user.domain.userBlock.QUserBlock.userBlock;
import static org.springframework.util.StringUtils.hasText;


public class MessageRepositoryImpl implements MessageRepositoryCustom {
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
                                        .selectFrom(userBlock.blockUserId)
                                        .innerJoin(userBlock.userId, user)
                                        .where(userBlock.userId.userId.eq(userId))})))
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

    @Override
    public Page<ReceiveMessageListDto> findAllByReceiverId(Long receiverId, Pageable pageable, Boolean notRead, String keyword) {
        List<ReceiveMessageListDto> results = queryFactory
                .select(Projections.constructor(ReceiveMessageListDto.class,
                        message.messageId,
                        message.title,
                        message.sender.username.as("sender"),
                        message.isRead,
                        message.createdAt
                ))
                .from(message)
                .innerJoin(message.sender, user)
                .where(message.deleted.ne(DeleteCondition.RECEIVER),
                        message.sender.notIn(
                                new JPQLQuery[]{JPAExpressions
                                        .selectFrom(userBlock.blockUserId)
                                        .innerJoin(userBlock.userId, user)
                                        .where(userBlock.userId.userId.eq(receiverId))}),
                        isSearchable(keyword),
                        isNotRead(notRead))
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(message)
                .where(message.deleted.ne(DeleteCondition.RECEIVER),
                        message.sender.notIn(
                                new JPQLQuery[]{JPAExpressions
                                        .selectFrom(userBlock.blockUserId)
                                        .innerJoin(userBlock.userId, user)
                                        .where(userBlock.userId.userId.eq(receiverId))}),
                        isSearchable(keyword),
                        isNotRead(notRead))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public Page<SendMessageListDto> findAllBySenderId(Long senderId, Pageable pageable, String keyword) {
        List<SendMessageListDto> results = queryFactory
                .select(Projections.constructor(SendMessageListDto.class,
                        message.messageId,
                        message.title,
                        message.receiver.username.as("receiver"),
                        message.readTime,
                        message.createdAt
                ))
                .from(message)
                .innerJoin(message.receiver, user)
                .where(message.deleted.ne(DeleteCondition.SENDER),
                        isSearchable(keyword))
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(message)
                .where(message.deleted.ne(DeleteCondition.SENDER),
                        isSearchable(keyword))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    BooleanExpression isSearchable(String keyword){
        if(!hasText(keyword))
            return null;
        return message.title.contains(keyword);
    }

    BooleanExpression isNotRead(Boolean notRead){
        if(notRead == null)
            return null;
        return message.isRead.eq(notRead);
    }
}
