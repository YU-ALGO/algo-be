package com.stock.yu.downbitbe.user.domain.user;

import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;

public interface UserAllergyInfoRepositoryCustom {
    AllergyInfoDto findAllergyDtoByUserId(Long userId);
}
