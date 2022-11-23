package com.stock.yu.downbitbe.food.domain;

import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;

@Entity
@Table(name = "FOOD_ALLERGY_INFO")
@Getter
@DynamicUpdate
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    public FoodAllergyInfo updateFoodAllergy(AllergyInfo allergyInfo) {
        this.allergyInfo = allergyInfo;
        return this;
    }
}
