package com.stock.yu.downbitbe.user.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
public class UserBlockListDto {
    private Long id;
    @JsonProperty("block_id")
    private String blockId;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    public UserBlockListDto(UserBlock userBlock) {
        this.id = userBlock.getUserBlockId();
        this.blockId = userBlock.getBlockUserId().getNickname();
        this.createdAt = userBlock.getCreatedAt();
    }
}
