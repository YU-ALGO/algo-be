package com.stock.yu.downbitbe.message.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.yu.downbitbe.user.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequestDto {
    @NotBlank
    private String title;
    @NotBlank
    private String content;
    @NotBlank
    @JsonProperty("receiver_name")
    private String receiverName;

    public Message toEntity(User sender, User receiver){
        return Message.builder()
                .title(title)
                .content(content)
                .sender(sender)
                .receiver(receiver)
                .build();
    }
}
