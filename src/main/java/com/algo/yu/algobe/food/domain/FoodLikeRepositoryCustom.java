package com.algo.yu.algobe.food.domain;

import com.algo.yu.algobe.user.domain.profile.UserFoodLikeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface FoodLikeRepositoryCustom {

    Page<UserFoodLikeResponseDto> getUserFoodLikeListByNickname(String nickname, Pageable pageable);
    List<Long> getFoodIdListByUsername(String username);
}
