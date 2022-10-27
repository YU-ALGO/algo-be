package com.stock.yu.downbitbe.food.domain;

import javax.persistence.*;

@Entity
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
}
