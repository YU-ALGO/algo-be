package com.algo.yu.algobe.food.ui;

import com.algo.yu.algobe.food.application.FoodService;
import com.algo.yu.algobe.food.domain.*;
import com.algo.yu.algobe.security.config.Config;
import com.algo.yu.algobe.user.application.UserService;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.domain.user.UserAuthDto;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Log4j2
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

    @PreAuthorize("isAuthenticated()")
    @GetMapping("")
    public ResponseEntity<List<FoodListResponseDto>> getFoodList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                                 @RequestParam(value = "keyword", required = false) String keyword, AllergyInfoDto allergyFilter,
                                                                 @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        Page<FoodListResponseDto> foodListResponse = foodService.findAllFoods(allergyFilter, pageable, keyword, auth.getUsername());
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(foodListResponse.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(foodListResponse.stream().collect(Collectors.toList()));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/{food_id}")
    public ResponseEntity<FoodResponseDto> getFood(@PathVariable("food_id") Long foodId,
                                                   @CookieValue("foodList") String viewCookie,
                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        User user = userService.findByUsername(auth.getUsername());
        FoodResponseDto responseDto = foodService.findFoodLikeByFoodId(foodId, user.getUserId());
        List<Long> viewList = new ArrayList<>();

        if (!(viewCookie.equals("") || viewCookie.equals("[]"))) {
            for (String s : viewCookie.replaceAll("[\\[\\]]", "").split("/")) {
                log.info(s);
                viewList.add(Long.parseLong(s));
            }
        }

        viewList.add(foodId);
        ResponseCookie foodListCookie = ResponseCookie.from("foodList", viewList.toString().replaceAll(" ", "").replaceAll(",", "/"))
                .httpOnly(true)
                .path("/")
                .domain(Config.DOMAIN)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, foodListCookie.toString())
                .body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{food_id}/allergies")
    public ResponseEntity<Map<String, Boolean>> getFoodAllergyInfo(@PathVariable("food_id") Long foodId) {
        return ResponseEntity.status(HttpStatus.OK).body(foodService.getFoodAllergies(foodId));
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/viewLists")
    public ResponseEntity<List<FoodListResponseDto>> getUserViewFood(@CookieValue(value = "foodList", required = false) String foodCookie,
                                                                     @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth) {
        List<Long> foodList = new ArrayList<>();
        if (foodCookie != null && !foodCookie.isBlank()) {
            for (String s : foodCookie.replaceAll("[\\[\\]]", "").split("/")) {
                foodList.add(Long.parseLong(s));
            }
        }

        List<FoodListResponseDto> viewListDto = foodService.getViewFoodList(foodList, auth.getUsername());
        return ResponseEntity.status(HttpStatus.OK).body(viewListDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @Transactional
    public ResponseEntity<Long> createFood(final @RequestBody @Valid FoodRequestDto foodRequestDto) {
        Long foodId = foodService.createFood(foodRequestDto);
        Food food = foodService.findFoodByFoodId(foodId);
        foodService.createFoodAllergy(food, foodRequestDto.getAllergy().toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(food.getFoodId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{food_id}")
    @Transactional
    public ResponseEntity<Long> updateFood(final @RequestBody @Valid FoodRequestDto foodRequestDto, @PathVariable("food_id") Long foodId) {
        Long ret = foodService.updateFood(foodRequestDto, foodId);
        foodService.updateFoodAllergy(foodId, foodRequestDto.getAllergy().toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{food_id}")
    public ResponseEntity<Long> deleteFood(@PathVariable("food_id") Long foodId) {
        Long ret = foodService.deleteFood(foodId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }
}