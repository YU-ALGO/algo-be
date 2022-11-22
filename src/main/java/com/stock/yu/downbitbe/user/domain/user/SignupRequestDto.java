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
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[~!@#$%^&*()+|=])[A-Za-z\\d~!@#$%^&*()+|=]{8,}$")
    private String password;
    @NotBlank
    @Pattern(regexp = "^[ㄱ-ㅎㅏ-ㅣ가-힣a-zA-Z]{2,15}$")
    private String nickname;
    private AllergyInfoDto allergyInfoDto;
}
