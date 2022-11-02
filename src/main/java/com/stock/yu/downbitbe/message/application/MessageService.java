package com.stock.yu.downbitbe.message.application;

import com.stock.yu.downbitbe.message.domain.*;
import com.stock.yu.downbitbe.user.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public Page<ReceiveMessageListDto> findAllMessagesByReceiver(Pageable pageable, Long receiverId, Boolean notRead, String keyword) {
        return messageRepository.findAllReceiveMessagesBy(receiverId, pageable, notRead, keyword);
    }

    @Transactional(readOnly = true)
    public Page<SendMessageListDto> findAllMessagesBySender(Pageable pageable, Long senderId, String keyword) {
        return messageRepository.findAllSendMessagesBy(senderId, pageable, keyword);
    }

    @Transactional(readOnly = true)
    public MessageDto findMessageByMessageId(Long messageId, Long userId){
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("쪽지가 존재하지 않습니다."));
        if(Objects.equals(message.getReceiver().getUserId(), userId)){
            if(message.getReadTime() == null) {
                messageRepository.save(message.updateRead());
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
}
