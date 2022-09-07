package com.stock.yu.downbitbe.message.service;

import com.stock.yu.downbitbe.message.dto.MessageDTO;
import com.stock.yu.downbitbe.message.entity.Message;
import com.stock.yu.downbitbe.user.entity.User;

import java.util.List;

public interface MessageService {
    Long register(MessageDTO messageDTO);
    MessageDTO get(Long id);
    void remove(Long id);
    List<MessageDTO> getAllWithWriter(Long senderId);

    default Message dtoToEntity(MessageDTO messageDTO) {
        Message message = Message.builder()
                .title(messageDTO.getTitle())
                .content(messageDTO.getContent())
                .sender(User.builder().id(messageDTO.getSenderId()).build())
                .build();

        return message;
    }

    default MessageDTO entityToDTO(Message message) {
        MessageDTO messageDTO = MessageDTO.builder()
                .id(message.getId())
                .title(message.getTitle())
                .content(message.getContent())
                .senderId(message.getSender().getId())
                .regDate(message.getCreatedAt())
                .modDate(message.getModifiedAt())
                .build();

        return messageDTO;
    }
}
