package com.stock.yu.downbitbe.domain.stock.entity;

import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "STOCK_VALUE")
@Getter
@NoArgsConstructor
public class StockValue {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private LocalDateTime date;

    @Column
    @NotNull
    private Integer value;

    @JoinColumn(name = "stock_id")
    @ManyToOne
    @NotNull
    private Stock stock;

    @Builder
    public StockValue(LocalDateTime date, Integer value, Stock stock){
        this.date = date;
        this.value = value;
        this.stock = stock;
    }
}
