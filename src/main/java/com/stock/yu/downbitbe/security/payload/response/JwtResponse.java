package com.stock.yu.downbitbe.security.payload.response;

import com.stock.yu.downbitbe.user.entity.Grade;
import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String userId;
    private Set<Grade> roles;
//    private String
}
