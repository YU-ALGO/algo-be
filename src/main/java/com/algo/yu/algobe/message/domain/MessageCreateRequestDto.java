package com.algo.yu.algobe.message.domain;

import com.algo.yu.algobe.user.domain.user.User;
import com.fasterxml.jackson.annotation.JsonProperty;
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
