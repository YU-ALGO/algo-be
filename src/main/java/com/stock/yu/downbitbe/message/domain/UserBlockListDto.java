package com.stock.yu.downbitbe.message.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserBlockListDto {
    private Long id;
    @JsonProperty("block_id")
    private String blockId;
}
