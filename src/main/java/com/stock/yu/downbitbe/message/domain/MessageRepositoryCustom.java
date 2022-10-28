package com.stock.yu.downbitbe.message.domain;

public interface MessageRepositoryCustom {
    Long countNonReadMessageByUserId(Long userId);
}
