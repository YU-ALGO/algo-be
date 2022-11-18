package com.stock.yu.downbitbe.user.domain.user;

import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@RequiredArgsConstructor
public class SignupRequestDto {
    @NotBlank
    @Email
    private String username;
    @NotBlank
    //@Pattern(regexp = )
    private String password;
    private String nickname;
    private AllergyInfoDto allergyInfoDto;
}
