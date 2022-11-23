package com.algo.yu.algobe.user.domain.user;

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
    private ResponseCookie foodListCookie;
    private ResponseCookie isLoginCookie;
    private ResponseCookie isAdminCookie;

}
