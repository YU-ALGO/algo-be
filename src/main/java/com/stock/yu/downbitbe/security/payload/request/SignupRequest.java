package com.stock.yu.downbitbe.security.payload.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupRequest {
    @JsonProperty("user_id")
    private String userId;
    private String password;
    private String nickname;
}
