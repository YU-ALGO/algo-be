package com.stock.yu.downbitbe.domain.stock.entity;

import com.stock.yu.downbitbe.domain.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "FAVORITE_STOCK")
@Getter
@NoArgsConstructor
public class FavoriteStock {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @NotNull
    private User user;

    @JoinColumn(name = "stock_id")
    @ManyToOne
    @NotNull
    private Stock stock;

    @Builder
    public FavoriteStock(User user, Stock stock){
        this.user = user;
        this.stock = stock;
    }
}
