package com.algo.yu.algobe.message.application;

import com.algo.yu.algobe.message.domain.*;
import com.algo.yu.algobe.user.domain.user.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Slf4j
@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public Page<ReceiveMessageListDto> findAllMessagesByReceiver(Pageable pageable, Long receiverId, Boolean notRead, String keyword, MessageSearchType searchType) {
        return messageRepository.findAllReceiveMessagesBy(receiverId, pageable, notRead, keyword, searchType);
    }

    @Transactional(readOnly = true)
    public Page<SendMessageListDto> findAllMessagesBySender(Pageable pageable, Long senderId, String keyword, MessageSearchType searchType) {
        return messageRepository.findAllSendMessagesBy(senderId, pageable, keyword, searchType);
    }

    @Transactional
    public MessageDto findMessageByMessageId(Long messageId, Long userId){
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("쪽지가 존재하지 않습니다."));
        if(Objects.equals(message.getReceiver().getUserId(), userId)){
            if(message.getReadTime() == null) {
                messageRepository.save(message.updateRead());
                log.info("message read = " + message.getReadTime());
            }
        }
        return new MessageDto(message);
    }

    @Transactional(readOnly = true)
    public Integer getNonReadMessageCount(Long userId){
        return messageRepository.countByIsRead(userId);
    }

    @Transactional
    public Long createMessage(MessageCreateRequestDto messageCreateRequestDto, User receiver, User sender) {
        Message message = messageCreateRequestDto.toEntity(sender, receiver);
        return messageRepository.save(message).getMessageId();
    }
    @Transactional
    public Long deleteMessage(Long messageId, Long userId){
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("쪽지가 존재하지 않습니다."));
        if(Objects.equals(message.getReceiver().getUserId(), userId)){
            if(message.getDeleted() == DeleteCondition.SENDER){
                messageRepository.delete(message);
            } else if(message.getDeleted() == DeleteCondition.NONE){
                int result = messageRepository.updateDeleteCondition(messageId, DeleteCondition.RECEIVER);
                if(result == 0)
                    throw new RuntimeException("삭제에 실패했습니다.");
            }
        } else if(Objects.equals(message.getSender().getUserId(), userId)){
            if(message.getDeleted() == DeleteCondition.RECEIVER){
                messageRepository.delete(message);
            } else if(message.getDeleted() == DeleteCondition.NONE){
                int result = messageRepository.updateDeleteCondition(messageId, DeleteCondition.SENDER);
                if(result == 0)
                    throw new RuntimeException("삭제에 실패했습니다.");
            }
        }
        return message.getMessageId();
    }

    @Transactional
    public Long deleteMessageListBySender(List<Long> messageIdList, Long userId){
        Long ret = messageRepository.deleteMessagesBySenderId(userId, messageIdList);
        ret += messageRepository.updateDeletedBySenderId(userId, messageIdList);
        return ret;
    }


    @Transactional
    public Long deleteMessageListByReceiver(List<Long> messageIdList, Long userId){
        Long ret = messageRepository.deleteMessagesByReceiverId(userId, messageIdList);
        ret += messageRepository.updateDeletedByReceiverId(userId, messageIdList);
        return ret;
    }
}
