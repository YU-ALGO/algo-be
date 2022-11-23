package com.algo.yu.algobe.user.domain.user;

import com.algo.yu.algobe.BaseTimeEntity;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "USER")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@DynamicUpdate
@DynamicInsert
@ToString
public class User extends BaseTimeEntity {
    @Id
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

   public void addGrade(Grade grade) {
        gradeSet.add(grade);
    }

    public User updatePassword(String password){
        if(password != null)
            this.password = password;
        return this;
    }

    public User updateNickname(String nickname) {
       if(nickname != null)
           this.nickname = nickname;
       return this;
    }

    public User updateIntroduce(String introduce) {
        if(introduce != null)
            this.introduce = introduce;
        return this;
    }

    public User updateProfileImage(String profileImageUrl){
       if(profileImageUrl != null)
           this.profileImg = profileImageUrl;
       return this;
    }

}