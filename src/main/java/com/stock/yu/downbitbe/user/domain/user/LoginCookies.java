package com.stock.yu.downbitbe.user.domain.user;

import lombok.*;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class LoginCookies {
    private ResponseCookie accessCookie;
    private ResponseCookie refreshCookie;
    private ResponseCookie viewListCookie;
    private ResponseCookie isLoginCookie;
    private ResponseCookie isAdminCookie;

}
