package com.stock.yu.downbitbe.message.domain;

public interface MessageRepositoryCustom {
    Integer countNonReadMessageByUserId(Long userId);

    Long updateDeleteCondition(Long messageId, DeleteCondition receiver);
}
