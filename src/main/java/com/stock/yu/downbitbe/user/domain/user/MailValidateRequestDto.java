package com.stock.yu.downbitbe.user.domain.user;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MailValidateRequestDto {
    String username;
    int code;
}
