package com.stock.yu.downbitbe.food.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

@Getter
@RequiredArgsConstructor
public class FoodSearchDto {
    String keyword;
    AllergyInfoDto allergyFilter;
}
