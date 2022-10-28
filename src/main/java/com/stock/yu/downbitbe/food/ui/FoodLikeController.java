package com.stock.yu.downbitbe.food.ui;


import com.stock.yu.downbitbe.food.application.FoodLikeService;
import com.stock.yu.downbitbe.user.dto.UserAuthDTO;
import com.stock.yu.downbitbe.user.entity.User;
import com.stock.yu.downbitbe.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foods/")
public class FoodLikeController {
    private final FoodLikeService foodLikeService;
    private final UserService userService;

    @PostMapping("{food_id}/likes")
    public ResponseEntity<Long> createFoodLike(@PathVariable("food_id") Long foodId,
                                               @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = foodLikeService.createFoodLike(foodId, user);
        int update_ret = foodLikeService.updateLike(foodId, user, 1);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @DeleteMapping("{food_id}/likes")
    public ResponseEntity<Long> deleteFoodLike(@PathVariable("food_id") Long foodId,
                                               @CurrentSecurityContext(expression = "authentication.principal") UserAuthDTO auth){
        User user = userService.findByUsername(auth.getUsername());
        Long ret = foodLikeService.deleteFoodLike(foodId, user);
        foodLikeService.updateLike(foodId, user, -1);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}
