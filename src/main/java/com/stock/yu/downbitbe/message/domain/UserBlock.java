package com.stock.yu.downbitbe.message.domain;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.user.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERBLOCK")
@Getter
@NoArgsConstructor
public class UserBlock extends BaseTimeEntity {
    @Id
    @Column(name = "user_block_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userBlockId;

    @JoinColumn(name = "user_id")
    @ManyToOne
    @NotNull
    private User userId;

    @JoinColumn(name = "block_user_id")
    @ManyToOne
    @NotNull
    private User blockUserId;

    @Builder
    public UserBlock(User user, User blockUserId){
        this.userId = user;
        this.blockUserId = blockUserId;
    }

}
