package com.stock.yu.downbitbe.security.payload.response;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String nickname;
    private String loginType = "Bearer";
    private String username;
    //@JsonProperty("is_admin")
    private Boolean isAdmin;
//    private String
}
