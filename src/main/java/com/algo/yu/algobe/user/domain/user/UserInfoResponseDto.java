package com.algo.yu.algobe.user.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserInfoResponseDto {
    private String nickname;
    private String username;
    private String loginType = "Bearer";
    //@JsonProperty("is_admin")
    private Boolean isAdmin;
}
