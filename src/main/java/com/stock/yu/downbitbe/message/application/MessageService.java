package com.stock.yu.downbitbe.message.application;

import com.stock.yu.downbitbe.message.domain.*;
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
    public Page<ReceiveMessageListDto> findAllMessagesByReceiver(Long receiverId, Boolean notRead, Pageable pageable) {
        return null;
    }

    @Transactional(readOnly = true)
    public Page<SendMessageListDto> findAllMessagesBySender(Long senderId, Pageable pageable) {
        return null;
    }

    @Transactional(readOnly = true)
    public MessageDto findMessageByMessageId(Long messageId, Long userId){
        Message message = messageRepository.findById(messageId).orElseThrow(() -> new IllegalArgumentException("쪽지가 존재하지 않습니다."));
        if(Objects.equals(message.getReceiver().getUserId(), userId)){
            if(message.getReadTime() == null) {
                message.updateTime();
            }
        }
        return new MessageDto(message);
    }

    @Transactional(readOnly = true)
    public Long getNonReadMessageCount(Long userId){
        Long messageCount = messageRepository.countNonReadMessageByUserId(userId);
        return null;
    }


//    default Message dtoToEntity(MessageDTO messageDTO) {
//        Message message = Message.builder()
//                .title(messageDTO.getTitle())
//                .content(messageDTO.getContent())
//                .sender(User.builder().id(messageDTO.getSenderId()).build())
//                .build();
//
//        return message;
//    }
//
//    default MessageDTO entityToDTO(Message message) {
//        MessageDTO messageDTO = MessageDTO.builder()
//                .id(message.getId())
//                .title(message.getTitle())
//                .content(message.getContent())
//                .senderId(message.getSender().getId())
//                .regDate(message.getCreatedAt())
//                .modDate(message.getModifiedAt())
//                .build();
//
//        return messageDTO;
//    }
}
