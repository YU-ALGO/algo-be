package com.stock.yu.downbitbe.food.ui;

import com.stock.yu.downbitbe.food.application.FoodLikeService;
import com.stock.yu.downbitbe.food.application.FoodRecommendService;
import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import com.stock.yu.downbitbe.food.domain.FoodListResponseDto;
import com.stock.yu.downbitbe.user.application.ProfileService;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@RequestMapping("/api/v1/foods/")
@RestController
@RequiredArgsConstructor
public class FoodRecommendController {

    private final FoodRecommendService foodRecommendService;
    private final ProfileService profileService;
    private final FoodLikeService foodLikeService;

    @PreAuthorize("isAuthenticated()")
    @GetMapping("recommendation")
    public ResponseEntity<?> getUserRecommendedFood(@CookieValue(value = "foodList", required = false) String foodCookie,
            @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        List<Long> foodList = new ArrayList<>();
        log.info("foodList : " + foodCookie);
        if (foodCookie != null && !foodCookie.isBlank()) {
            for (String s : foodCookie.replaceAll("[\\[\\]]", "").split("/")) {
                foodList.add(Long.parseLong(s));
            }
        }

        List<Long> likeList = foodLikeService.getFoodIdListByUsername(auth.getUsername());
        log.info("likeList : " + likeList);
        AllergyInfoDto allergyInfoDto = profileService.getUserAllergyByNickname(auth.getNickname());
        log.info("allergyInfoDto" + allergyInfoDto);
        List<Long> recommendList = foodRecommendService.getRecommendedFoodList(likeList, foodList, allergyInfoDto);
        log.info("recommendList : " + recommendList);


        List<FoodListResponseDto> recommendListDto = foodRecommendService.getRecommendedFoodListDto(recommendList);

        return ResponseEntity.ok(recommendListDto);
    }
}
