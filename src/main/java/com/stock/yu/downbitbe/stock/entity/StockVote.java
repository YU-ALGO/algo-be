package com.stock.yu.downbitbe.stock.entity;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.user.entity.User;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "STOCK_VOTE")
@Getter
@NoArgsConstructor
public class StockVote extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "vote")
    @NotNull
    @Enumerated(EnumType.STRING)
    private VoteType type;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @NotNull
    private User user;

    @JoinColumn(name = "stock_id")
    @ManyToOne
    @NotNull
    private Stock stock;

    @Builder
    public StockVote(VoteType type, User user, Stock stock){
        this.type = type;
        this.user = user;
        this.stock = stock;
    }

}
