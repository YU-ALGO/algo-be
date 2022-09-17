package com.stock.yu.downbitbe.food.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Embeddable
public class AllergyInfo {
    @Column(name = "WHEAT")
    private Boolean wheat;

    @Column(name = "MILK")
    private Boolean milk;

    @Column(name = "BREAD")
    private Boolean bread;
}
