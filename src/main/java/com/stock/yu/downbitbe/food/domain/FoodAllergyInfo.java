package com.stock.yu.downbitbe.food.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "FOOD_ALLERGY_INFO")
public class FoodAllergyInfo {
    @Id
    @Column(name = "food_id")
    private Long foodId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "food_id")
    private Food food;

    @Embedded
    private AllergyInfo allergyInfo;

    @Builder
    public FoodAllergyInfo(Long foodId, Food food, AllergyInfo allergyInfo){
        this.foodId = foodId;
        this.food = food;
        this.allergyInfo = allergyInfo;
    }
}
