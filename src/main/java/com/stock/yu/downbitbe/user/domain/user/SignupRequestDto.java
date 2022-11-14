package com.stock.yu.downbitbe.user.domain.user;

import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class SignupRequestDto {
    private String username;
    private String password;
    private String nickname;
    private AllergyInfoDto allergyInfoDto;
}
