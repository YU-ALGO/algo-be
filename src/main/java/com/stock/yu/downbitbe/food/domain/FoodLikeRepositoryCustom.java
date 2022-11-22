package com.stock.yu.downbitbe.food.domain;

import com.stock.yu.downbitbe.user.domain.profile.UserFoodLikeResponseDto;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodLikeRepositoryCustom {

    Page<UserFoodLikeResponseDto> getUserFoodLikeListByNickname(String nickname, Pageable pageable);
    List<Long> getFoodIdListByUsername(String username);
}
