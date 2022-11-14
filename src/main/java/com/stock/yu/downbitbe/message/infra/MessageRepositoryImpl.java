package com.stock.yu.downbitbe.message.infra;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.message.domain.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;


import java.util.List;

import static com.stock.yu.downbitbe.message.domain.QMessage.message;
import static com.stock.yu.downbitbe.user.domain.userBlock.QUserBlock.userBlock;
import static org.springframework.util.StringUtils.hasText;

@Slf4j
@Repository
@RequiredArgsConstructor
public class MessageRepositoryImpl implements MessageRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Integer countByIsRead(Long userId) {
        return queryFactory
                .selectFrom(message)
                .where(message.receiver.userId.eq(userId),
                        message.readTime.isNull(),
                        message.deleted.ne(DeleteCondition.RECEIVER),
                        (message.sender.notIn(
                                new JPQLQuery[]{JPAExpressions
                                        .select(userBlock.blockUserId)
                                        .from(userBlock)
                                        .where(userBlock.userId.userId.eq(userId),
                                        message.createdAt.after(userBlock.createdAt))})))
                .fetch().size();
    }

    @Override
    public Page<ReceiveMessageListDto> findAllReceiveMessagesBy(Long receiverId, Pageable pageable, Boolean notRead, String keyword, MessageSearchType searchType) {
        List<ReceiveMessageListDto> results = queryFactory
                .select(Projections.constructor(ReceiveMessageListDto.class,
                        message.messageId,
                        message.title,
                        message.sender.nickname.as("sender"),
                        message.isRead,
                        message.createdAt
                ))
                .from(message)
                .where(message.receiver.userId.eq(receiverId),
                        message.deleted.ne(DeleteCondition.RECEIVER),
                        message.sender.notIn(
                                new JPQLQuery[]{JPAExpressions
                                        .select(userBlock.blockUserId)
                                        .from(userBlock)
                                        .where(userBlock.userId.userId.eq(receiverId),
                                        message.createdAt.after(userBlock.createdAt))}),
                        isSearchable(keyword, searchType),
                        isNotRead(notRead))
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(message)
                .where(message.receiver.userId.eq(receiverId),
                        message.deleted.ne(DeleteCondition.RECEIVER),
                        message.sender.notIn(
                                new JPQLQuery[]{JPAExpressions
                                        .select(userBlock.blockUserId)
                                        .from(userBlock)
                                        .where(userBlock.userId.userId.eq(receiverId),
                                        message.createdAt.after(userBlock.createdAt))}),
                        isSearchable(keyword, searchType),
                        isNotRead(notRead))
                .fetch().size();
        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public Page<SendMessageListDto> findAllSendMessagesBy(Long senderId, Pageable pageable, String keyword, MessageSearchType searchType) {
        List<SendMessageListDto> results = queryFactory
                .select(Projections.constructor(SendMessageListDto.class,
                        message.messageId,
                        message.title,
                        message.receiver.nickname.as("receiver"),
                        message.readTime,
                        message.createdAt
                ))
                .from(message)
                .where(message.sender.userId.eq(senderId),
                        message.deleted.ne(DeleteCondition.SENDER),
                        isSearchable(keyword, searchType))
                .orderBy(message.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(message)
                .where(message.sender.userId.eq(senderId),
                        message.deleted.ne(DeleteCondition.SENDER),
                        isSearchable(keyword, searchType))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public Long deleteMessagesBySenderId(Long senderId, List<Long> messageIdList) {
        int totalSize = queryFactory
                .selectFrom(message)
                .where(message.sender.userId.eq(senderId)
                        .and(message.messageId.in(messageIdList))
                        .and(message.deleted.eq(DeleteCondition.RECEIVER)))
                .fetch().size();

        log.info("totalSize=", totalSize);
        Long results =  queryFactory
                .delete(message)
                .where(message.sender.userId.eq(senderId)
                        .and(message.messageId.in(messageIdList))
                        .and(message.deleted.eq(DeleteCondition.RECEIVER)))
                .execute();



        return results;
    }

    @Override
    public Long deleteMessagesByReceiverId(Long receiverId, List<Long> messageIdList) {
        return queryFactory
                .delete(message)
                .where(message.receiver.userId.eq(receiverId)
                        .and(message.messageId.in(messageIdList))
                        .and(message.deleted.eq(DeleteCondition.SENDER)))
                .execute();
    }

    @Override
    public Long updateDeletedBySenderId(Long senderId, List<Long> messageIdList) {
        int totalSize = queryFactory
                .selectFrom(message)
                .where(message.sender.userId.eq(senderId)
                        .and(message.messageId.in(messageIdList))
                        .and(message.deleted.eq(DeleteCondition.NONE)))
                .fetch().size();

        log.info("totalSize="+totalSize);

        return queryFactory
                .update(message)
                .set(message.deleted, DeleteCondition.SENDER)
                .where(message.sender.userId.eq(senderId)
                        .and(message.messageId.in(messageIdList))
                        .and(message.deleted.eq(DeleteCondition.NONE)))
                .execute();
    }

    @Override
    public Long updateDeletedByReceiverId(Long receiverId, List<Long> messageIdList) {
        log.info("listSize="+messageIdList);
        List<Message> totalSize = queryFactory
                .selectFrom(message)
                .where(message.receiver.userId.eq(receiverId))
                        //.and(message.messageId.in(messageIdList)))
                        //.and(message.deleted.eq(DeleteCondition.NONE)))
                .fetch();

        log.info("totalSize="+totalSize);


        return queryFactory
                .update(message)
                .set(message.deleted, DeleteCondition.RECEIVER)
                .where(message.receiver.userId.eq(receiverId)
                        .and(message.messageId.in(messageIdList))
                        .and(message.deleted.eq(DeleteCondition.NONE)))
                .execute();
    }

    BooleanExpression isSearchable(String keyword, MessageSearchType searchType){
        if(!hasText(keyword) || searchType == null)
            return null;

        if(searchType == MessageSearchType.TITLE)
            return message.title.contains(keyword);
        else if(searchType == MessageSearchType.RECEIVER)
            return message.receiver.nickname.eq(keyword);
        else return message.sender.nickname.eq(keyword);
    }

    BooleanExpression isNotRead(Boolean notRead){
        if(notRead == null || !notRead)
            return null;
        return message.isRead.ne(true);
    }
}
