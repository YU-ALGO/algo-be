package com.stock.yu.downbitbe.message.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MessageRepositoryCustom {
    Integer countByIsRead(Long receiverId);

    Page<ReceiveMessageListDto> findAllReceiveMessagesBy(Long receiverId, Pageable pageable, Boolean notRead, String keyword);

    Page<SendMessageListDto> findAllSendMessagesBy(Long senderId, Pageable pageable, String keyword);
}
