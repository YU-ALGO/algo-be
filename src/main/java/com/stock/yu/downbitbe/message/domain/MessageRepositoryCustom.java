package com.stock.yu.downbitbe.message.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {
    Integer countNonReadMessageByUserId(Long userId);

    Long updateDeleteCondition(Long messageId, DeleteCondition receiver);

    Page<ReceiveMessageListDto> findAllByReceiverId(Long receiverId, Pageable pageable, Boolean notRead, String keyword);

    Page<SendMessageListDto> findAllBySenderId(Long senderId, Pageable pageable, String keyword);
}
