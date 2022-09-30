package com.stock.yu.downbitbe.user.entity;

import com.stock.yu.downbitbe.food.domain.AllergyInfo;

import javax.persistence.*;

@Entity
@Table(name = "USER_ALLERGY_INFO")
public class UserAllergyInfo {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private AllergyInfo allergyInfo;
}