package com.stock.yu.downbitbe.domain.user.entity;

import com.stock.yu.downbitbe.domain.BaseTimeEntity;
import com.sun.istack.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "USER")
@Getter
@NoArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @NotNull
    private String user_id;

    @Column
    @NotNull
    @Enumerated(EnumType.STRING)
    private LoginType type;

    @Column
    @NotNull
    private String nickname;

    @Column
    private float hit_rate;

    @Column
    @Enumerated(EnumType.STRING)
    private Grade grade;

    @Builder
    public User(String user_id, LoginType type, String nickname, float hit_rate, Grade grade){
        this.user_id = user_id;
        this.type= type;
        this.nickname = nickname;
        this.hit_rate = hit_rate;
        this.grade = grade;
    }

}
