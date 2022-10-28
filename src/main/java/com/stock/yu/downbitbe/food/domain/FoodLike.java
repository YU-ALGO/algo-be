package com.stock.yu.downbitbe.food.domain;

import com.stock.yu.downbitbe.user.domain.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "FOOD_LIKE")
@Getter
@NoArgsConstructor
public class FoodLike {
    @Id
    @Column(name= "food_like_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long foodLikeId;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private User user;

    @JoinColumn(name = "food_id")
    @ManyToOne
    @OnDelete(action = OnDeleteAction.CASCADE)
    @NotNull
    private Food food;

    @Builder
    public FoodLike(User user, Food food){
        this.user = user;
        this.food = food;
    }
}
