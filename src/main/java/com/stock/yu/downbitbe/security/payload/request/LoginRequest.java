package com.stock.yu.downbitbe.security.payload.request;

import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class LoginRequest {
    private String username;
    private String password;
}
