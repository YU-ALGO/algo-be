package com.stock.yu.downbitbe.user.domain.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserInfoResponseDto {
    private String nickname;
    private String loginType = "Bearer";
    private String username;
    //@JsonProperty("is_admin")
    private Boolean isAdmin;
//    private String
}
