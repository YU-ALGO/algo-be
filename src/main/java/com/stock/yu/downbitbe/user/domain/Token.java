package com.stock.yu.downbitbe.user.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Token {
    private String refreshToken;
    private String accessToken;
}
