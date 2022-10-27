package com.stock.yu.downbitbe.message.service;

import com.stock.yu.downbitbe.message.dto.MessageDTO;
import com.stock.yu.downbitbe.message.entity.Message;
import com.stock.yu.downbitbe.message.repository.MessageRepository;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MessageService {

    private final MessageRepository messageRepository;

    @Transactional(readOnly = true)
    public List<MessageDTO> findAllMessageBySender(long senderId) {
        return messageRepository.findAllBySender(senderId);
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
