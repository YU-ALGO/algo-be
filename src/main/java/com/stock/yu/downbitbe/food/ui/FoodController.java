package com.stock.yu.downbitbe.food.ui;

import com.stock.yu.downbitbe.food.application.FoodService;
import com.stock.yu.downbitbe.food.domain.*;
import com.stock.yu.downbitbe.security.config.Config;
import com.stock.yu.downbitbe.user.domain.user.UserAuthDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import com.stock.yu.downbitbe.user.application.UserService;
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

    @GetMapping("")
    public ResponseEntity<List<FoodListResponseDto>> getFoodList(@PageableDefault(sort = "createdAt", direction = Sort.Direction.DESC) Pageable pageable,
                                                             @RequestParam(value = "keyword", required = false) String keyword, AllergyInfoDto allergyFilter){
        Page<FoodListResponseDto> foodListResponse = foodService.findAllFoods(allergyFilter, pageable, keyword);
        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("X-Page-Count", String.valueOf(foodListResponse.getTotalPages()));
        return ResponseEntity.status(HttpStatus.OK).headers(responseHeaders).body(foodListResponse.stream().collect(Collectors.toList()));
    }

    @GetMapping("/{food_id}")
    public ResponseEntity<FoodResponseDto> getFood(@PathVariable("food_id") Long foodId,
                                                   @CookieValue("foodList") String viewCookie,
                                                   @CurrentSecurityContext(expression = "authentication.principal") UserAuthDto auth){
        User user = userService.findByUsername(auth.getUsername());
        FoodResponseDto responseDto = foodService.findFoodLikeByFoodId(foodId, user.getUserId());
        List<Long> viewList = new ArrayList<>();

        if(!(viewCookie.equals("")||viewCookie.equals("[]"))) {
            for (String s : viewCookie.replaceAll("[\\[\\]]", "").split("/")) {
                log.info(s);
                viewList.add(Long.parseLong(s));
            }
        }

        viewList.add(foodId);
        ResponseCookie foodListCookie = ResponseCookie.from("foodList",viewList.toString().replaceAll(" ", "").replaceAll(",","/"))
                .httpOnly(true)
                .path("/")
                .domain(Config.DOMAIN)
                .build();

        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.SET_COOKIE, foodListCookie.toString())
                .body(responseDto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("")
    @Transactional
    public ResponseEntity<Long> createFood(final @RequestBody @Valid FoodRequestDto foodRequestDto){
        Long foodId = foodService.createFood(foodRequestDto);
        Food food = foodService.findFoodByFoodId(foodId);
        foodService.createFoodAllergy(food, foodRequestDto.getAllergy().toEntity());
        return ResponseEntity.status(HttpStatus.OK).body(food.getFoodId());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{food_id}")
    public ResponseEntity<Long> updateFood(final @RequestBody @Valid FoodRequestDto foodRequestDto, @PathVariable("food_id") Long foodId){
        Long ret = foodService.updateFood(foodRequestDto, foodId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{food_id}")
    public ResponseEntity<Long> deleteFood(@PathVariable("food_id") Long foodId){
        Long ret = foodService.deleteFood(foodId);
        return ResponseEntity.status(HttpStatus.OK).body(ret);
    }


}
