package com.stock.yu.downbitbe.food.domain;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FoodRequestDto {
    @NotBlank
    @JsonProperty("food_name")
    private String foodName;
    @NotNull
    private Long code;
    @NotBlank
    private String nutrition;
    @JsonProperty("food_image_url")
    private String foodImageUrl;


    public Food toEntity() {
        return Food.builder()
                .foodName(foodName)
                .code(code)
                .nutrition(nutrition)
                .foodImageUrl(foodImageUrl)
                .build();
    }
}
