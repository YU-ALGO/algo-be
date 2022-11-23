package com.algo.yu.algobe.user.domain.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MailRequestDto {
    private String username;
    @JsonProperty("is_signup")
    private Boolean isSignup;
}
