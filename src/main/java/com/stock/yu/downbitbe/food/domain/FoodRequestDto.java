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
    @JsonProperty("raw_materials")
    @NotBlank
    private String rawMaterials;
    @JsonProperty("product_kind")
    @NotBlank
    private String productKind;
    @JsonProperty("food_image_url")
    private String foodImageUrl;
    private AllergyInfoDto allergy;


    public Food toEntity() {
        return Food.builder()
                .foodName(foodName)
                .code(code)
                .nutrition(nutrition)
                .rawMaterials(rawMaterials)
                .productKind(productKind)
                .foodImageUrl(foodImageUrl)
                .build();
    }
}
