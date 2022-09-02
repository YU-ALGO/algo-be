package com.stock.yu.downbitbe.user.entity;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@ToString
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //TODO : 소셜 로그인 아이디 저장방식 및 엔티티 자체 아이디 중복허용 결정?
    @NotNull
    @Column(length = 30, nullable = false, name = "user_id")
    private String userId;

    //@NotNull
    @Column
    private String password;

    @Column
    //@NotNull
    @Enumerated(EnumType.STRING)
    private LoginType type;

    @Column
    //@NotNull
    private String nickname;

    //TODO : 주식 예측률이므로 시큐리티 구현 후 삭제
    @Column(name = "hit_rate")
    private float hitRate;

//    @Column
//    @Enumerated(EnumType.STRING)
//    private Grade grade;
//
//    @Builder
//    public User(String userId, String password, LoginType type, String nickname, float hitRate, Grade grade){
//        this.userId = userId;
//        this.password = password;
//        this.type= type;
//        this.nickname = nickname;
//        this.hitRate = hitRate;
//        this.grade = grade;
//    }

    @Builder
    public User(String userId, String password, LoginType type, String nickname, float hitRate){
        this.userId = userId;
        this.password = password;
        this.type= type;
        this.nickname = nickname;
        this.hitRate = hitRate;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Grade> gradeSet = new HashSet<>();

    public void addGrade(Grade grade) {
        gradeSet.add(grade);
    }

}


