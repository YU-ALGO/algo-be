package com.stock.yu.downbitbe.user.domain.user;

import com.stock.yu.downbitbe.BaseTimeEntity;
import lombok.*;
import net.bytebuddy.implementation.bind.annotation.Default;
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
    @Column(name = "username")
    private String username;

    @NotNull
    @Min(100000)
    @Max(999999)
    @Column
    private int code;

    @NotNull
    @Column(name = "is_validate", columnDefinition = "tinyint(1) default 0")
    private boolean isValidate;

    public MailCode updateIsValidate() {
        this.isValidate = true;
        return this;
    }

}
