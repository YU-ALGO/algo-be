package com.algo.yu.algobe.user.domain.user;

import com.algo.yu.algobe.BaseTimeEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(name = "USER_TOKEN")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserToken extends BaseTimeEntity {
    @Id
    @Column(name = "username")
    private String username;

    @NotNull
    @Column
    private String refreshToken;

    @Column(name = "valid_time")
    private LocalDateTime validTime;

}
