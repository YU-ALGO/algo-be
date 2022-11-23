package com.stock.yu.downbitbe.user.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class SocialUserResponseDto {
    private String username;
    private String nickname;
}
