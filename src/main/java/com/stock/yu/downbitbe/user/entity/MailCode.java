package com.stock.yu.downbitbe.user.entity;

import com.stock.yu.downbitbe.BaseTimeEntity;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "USERMAIL")
@Getter
@DynamicUpdate
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MailCode extends BaseTimeEntity {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id")
    private User user;

    @NotNull
    @Min(100000)
    @Max(999999)
    private int code;

}
