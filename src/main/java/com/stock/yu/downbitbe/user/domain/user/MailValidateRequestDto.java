package com.stock.yu.downbitbe.user.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MailValidateRequestDto {
    String username;
    int code;
    @JsonProperty("is_signup")
    Boolean isSignup;
}
