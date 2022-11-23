package com.algo.yu.algobe.user.domain.user;

import com.algo.yu.algobe.food.domain.AllergyInfoDto;

public interface UserAllergyInfoRepositoryCustom {
    AllergyInfoDto findAllergyDtoByUserId(Long userId);
}
