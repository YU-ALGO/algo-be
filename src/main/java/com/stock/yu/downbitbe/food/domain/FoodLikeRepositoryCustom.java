package com.stock.yu.downbitbe.food.domain;

import com.stock.yu.downbitbe.user.domain.profile.UserFoodLikeResponseDto;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;

import java.util.List;

public interface FoodLikeRepositoryCustom {

    List<UserFoodLikeResponseDto> getUserFoodLikeListByNickname(String nickname);
    List<Long> getFoodIdListByUsername(String username);
}
