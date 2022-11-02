package com.stock.yu.downbitbe.user.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String nickname;
    private String loginType = "Bearer";
    private String username;
    //@JsonProperty("is_admin")
    private Boolean isAdmin;
}
