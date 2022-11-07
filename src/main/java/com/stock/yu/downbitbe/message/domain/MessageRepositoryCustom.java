package com.stock.yu.downbitbe.message.domain;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface MessageRepositoryCustom {
    Integer countByIsRead(Long receiverId);

    Page<ReceiveMessageListDto> findAllReceiveMessagesBy(Long receiverId, Pageable pageable, Boolean notRead, String keyword, MessageSearchType searchType);

    Page<SendMessageListDto> findAllSendMessagesBy(Long senderId, Pageable pageable, String keyword, MessageSearchType searchType);

    Long deleteMessagesBySenderId(Long senderId, List<Long> messageIdList);

    Long deleteMessagesByReceiverId(Long receiverId, List<Long> messageIdList);

    Long updateDeletedBySenderId(Long senderId, List<Long> messageIdList);

    Long updateDeletedByReceiverId(Long receiverId, List<Long> messageIdList);
}
