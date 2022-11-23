package com.algo.yu.algobe.message.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class SendMessageListDto {
    private Long id;
    private String title;
    private String receiver;
    @JsonProperty("read_time")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime readTime;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @QueryProjection
    public SendMessageListDto(Long id, String title, String receiver, LocalDateTime readTime, LocalDateTime createdAt) {
        this.id = id;
        this.title = title;
        this.receiver = receiver;
        this.readTime = readTime;
        this.createdAt = createdAt;
    }
}
