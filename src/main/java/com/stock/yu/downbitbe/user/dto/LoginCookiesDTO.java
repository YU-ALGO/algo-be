package com.stock.yu.downbitbe.user.dto;

import lombok.*;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginCookiesDTO {
    private ResponseCookie accessCookie;
    private ResponseCookie refreshCookie;
    private ResponseCookie viewListCookie;
    private ResponseCookie isLoginCookie;
    private ResponseCookie isAdminCookie;
}
