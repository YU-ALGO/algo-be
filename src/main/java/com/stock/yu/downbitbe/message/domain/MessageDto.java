package com.stock.yu.downbitbe.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.time.LocalDateTime;

@Getter
public class MessageDto {
    private final Long id;
    private final String title;
    private final String content;
    @JsonProperty("sender_name")
    private final String senderName;
    @JsonProperty("receiver_name")
    private final String receiverName;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private final LocalDateTime createdAt;

    public MessageDto(Message message){
        this.id = message.getMessageId();
        this.title = message.getTitle();
        this.content = message.getContent();
        this.senderName = message.getSender().getNickname();
        this.receiverName = message.getReceiver().getNickname();
        this.createdAt = message.getCreatedAt();
    }
}