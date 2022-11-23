package com.algo.yu.algobe.food.domain;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AllergyInfo {
    @Column(name = "SQUID", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean squid = false;

    @Column(name = "EGGS", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean eggs = false;

    @Column(name = "CHICKEN", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean chicken = false;

    @Column(name = "WHEAT", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean wheat = false;

    @Column(name = "NUTS", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean nuts = false;

    @Column(name = "MILK", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean milk = false;

    @Column(name = "PORK", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean pork = false;

    @Column(name = "BEEF", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean beef = false;

    @Column(name = "CLAMS", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean clams = false;

    @Column(name = "SULPHITE", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean sulphite = false;

    @Column(name = "BUCKWHEAT", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean buckwheat = false;

    @Column(name = "CRAB", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean crab = false;

    @Column(name = "SHRIMP", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean shrimp = false;

    @Column(name = "SOYBEAN", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean soybean = false;

    @Column(name = "TOMATO", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean tomato = false;

    @Column(name = "FISH", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean fish = false;

    @Column(name = "SESAME", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean sesame = false;

    @Column(name = "FRUIT", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean fruit = false;

    @Column(name = "GARLIC", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean garlic = false;

    @Column(name = "VEGETABLE", columnDefinition = "tinyint(1) default 0")
    @NotNull
    private Boolean vegetable = false;

}
