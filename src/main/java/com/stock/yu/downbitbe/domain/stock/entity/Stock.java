package com.stock.yu.downbitbe.domain.stock.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "STOCK")
@NoArgsConstructor
@Getter
public class Stock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "stock_code")
    @NotNull
    private String code;

    @Column(name = "up_count")
    @NotNull
    private Integer up;

    @Column(name = "down_count")
    @NotNull
    private Integer down;

    @Builder
    public Stock(String code, Integer up, Integer down){
        this.code = code;
        this.up = up;
        this.down = down;
    }
}
