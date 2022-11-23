package com.algo.yu.algobe.user.domain.user;

import com.algo.yu.algobe.food.domain.AllergyInfo;
import lombok.*;

import javax.persistence.*;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.TreeMap;

@Entity
@Table(name = "USER_ALLERGY_INFO")
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAllergyInfo {
    @Id
    @Column(name = "user_id")
    private Long userId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    @Embedded
    private AllergyInfo allergyInfo;

    public Map<String, Boolean> toTrueMap() {
        Field[] fields = this.allergyInfo.getClass().getDeclaredFields();
        TreeMap<String, Boolean> allergyInfoMap = new TreeMap<>();
        for(Field field : fields) {
            try {
                boolean value = (Boolean) field.get(this.allergyInfo);
                if(value)
                    allergyInfoMap.put(field.getName(), (Boolean) field.get(this.allergyInfo));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return allergyInfoMap;
    }

    public Map<String, Boolean> toMap() {
        Field[] fields = this.allergyInfo.getClass().getDeclaredFields();
        TreeMap<String, Boolean> allergyInfoMap = new TreeMap<>();
        for(Field field : fields) {
            try {
                allergyInfoMap.put(field.getName(), (Boolean) field.get(this.allergyInfo));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }

        return allergyInfoMap;
    }

    public UserAllergyInfo updateAllergyInfo(AllergyInfo allergyInfo) {
        if(allergyInfo != null)
            this.allergyInfo = allergyInfo;
        return this;
    }
}