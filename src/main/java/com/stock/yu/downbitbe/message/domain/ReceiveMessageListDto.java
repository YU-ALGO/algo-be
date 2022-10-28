package com.stock.yu.downbitbe.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class ReceiveMessageListDto {
    private Long id;
    private String title;
    private String sender;
    @JsonProperty("is_read")
    private Boolean isRead;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @QueryProjection
    public ReceiveMessageListDto(Long id, String title, String sender, Boolean isRead, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.sender = sender;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
