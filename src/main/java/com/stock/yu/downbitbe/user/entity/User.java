package com.stock.yu.downbitbe.user.entity;

import com.stock.yu.downbitbe.BaseTimeEntity;
import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "USER")
//@SecondaryTable(name = "USER_ALLERGY_INFO", pkJoinColumns = @PrimaryKeyJoinColumn(name = "USER_ID"))
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class User extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    //TODO : 소셜 로그인 아이디 저장방식 및 엔티티 자체 아이디 중복허용 결정?
    @NotNull
    @Column(length = 30)
    private String username;

    @NotNull
    @Column
    private String password;

    @Column(name = "login_type")
    @NotNull
    @Enumerated(EnumType.STRING)
    private LoginType loginType;

    @Column(length = 30)
    @NotNull
    private String nickname;

    @Column(length = 100)
    private String introduce;

    @Column(name = "profile_img")
    private String profileImg;

    // TODO : 알레르기 정보
//    @Column
//    @Enumerated(EnumType.STRING)
//    private Grade grade;

    public User(String username, String password, LoginType loginType, String nickname){
        this.username = username;
        this.password = password;
        this.loginType= loginType;
        this.nickname = nickname;
    }

    @ElementCollection(fetch = FetchType.EAGER)
    @Builder.Default
    private Set<Grade> gradeSet = new HashSet<>();

    //TODO : 프로필 사진

//    @ElementCollection
//    @Builder.Default
//    @CollectionTable(name = "user_allergy_info", joinColumns = @JoinColumn(name = "user_id"))
//    private Set<AllergyInfo> allergyInfoList = new HashSet<>();

//    AllergyInfo allergyInfo;

    //public void addAllergyInfo(AllergyInfo allergyInfo) {allergyInfoList.add(allergyInfo);}

   public void addGrade(Grade grade) {
        gradeSet.add(grade);
    }
}


