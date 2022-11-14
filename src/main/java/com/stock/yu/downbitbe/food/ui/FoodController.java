package com.stock.yu.downbitbe.food.ui;

import com.stock.yu.downbitbe.food.application.FoodService;
import com.stock.yu.downbitbe.food.domain.FoodListResponseDto;
import com.stock.yu.downbitbe.food.domain.FoodRequestDto;
import com.stock.yu.downbitbe.food.domain.FoodResponseDto;
import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.security.utils.JWTUtil;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.application.UserService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/foods")
@ApiResponses({
        @ApiResponse(responseCode = "200", description = "OK"),
        @ApiResponse(responseCode = "400", description = "BAD REQUEST"),
        @ApiResponse(responseCode = "404", description = "NOT FOUND"),
        @ApiResponse(responseCode = "500", description = "INTERNAL SERVER ERROR")
})
public class FoodController {
    private final FoodService foodService;
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<List<FoodListResponseDto>> getFoodList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                 @RequestParam(value = "keyword", required = false) String keyword, Map<String, Boolean> allergyFilter){
        Page<FoodListResponseDto> foodListResponse = foodService.findAllFoods(allergyFilter, pageable, keyword);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(foodListResponse.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(foodListResponse.stream().collect(Collectors.toList()));
    }

    @GetMapping("/{food_id}")
    public ResponseEntity<FoodResponseDto> getFood(@PathVariable("food_id") Long foodId,
                                                   @CookieValue("foodList") Set<Long> viewList,
                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        FoodResponseDto responseDto = foodService.findFoodByFoodId(foodId, user.getUserId());

        viewList.add(foodId);
        ResponseCookie foodListCookie = ResponseCookie.from("foodList",viewList.toString())
                .httpOnly(true)
                .path("/food")
                .domain(Config.DOMAIN)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, foodListCookie.toString())
                .body(responseDto);
    }

    @PostMapping("")
    public ResponseEntity<Long> createFood(final @RequestBody @Valid FoodRequestDto foodRequestDto){
        Long ret = foodService.createFood(foodRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PatchMapping("/{food_id}")
    public ResponseEntity<Long> updateFood(final @RequestBody @Valid FoodRequestDto foodRequestDto, @PathVariable("food_id") Long foodId){
        Long ret = foodService.updateFood(foodRequestDto, foodId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @DeleteMapping("/{food_id}")
    public ResponseEntity<Long> deleteFood(@PathVariable("food_id") Long foodId){
        Long ret = foodService.deleteFood(foodId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }


}
