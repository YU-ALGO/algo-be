package com.stock.yu.downbitbe.food.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter
@ToString
@NoArgsConstructor
public class AllergyInfo {
    @Column(name = "WHEAT")
    @NotNull
    @ColumnDefault("0")
    private Boolean wheat;

    @Column(name = "MILK")
    @NotNull
    @ColumnDefault("0")
    private Boolean milk;

    @Column(name = "BREAD")
    @NotNull
    @ColumnDefault("0")
    private Boolean bread;
}
