package com.stock.yu.downbitbe.security.payload.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.yu.downbitbe.domain.user.entity.Grade;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String userId;
    //@JsonProperty("is_admin")
    private Boolean isAdmin;
//    private String
}
