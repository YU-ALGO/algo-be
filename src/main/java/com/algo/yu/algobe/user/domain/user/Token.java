package com.algo.yu.algobe.user.domain.user;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Token {
    private String refreshToken;
    private String accessToken;
    private Integer code;
}
