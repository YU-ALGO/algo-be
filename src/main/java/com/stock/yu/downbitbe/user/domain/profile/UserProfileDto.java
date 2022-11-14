package com.stock.yu.downbitbe.user.domain.profile;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.stock.yu.downbitbe.food.domain.AllergyInfo;
import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@RequiredArgsConstructor
public class UserProfileDto extends UserProfileModifyDto {
    private String nickname;
    private String username;
    @JsonProperty("reg_date")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private LocalDateTime regDate;
    private String profileImg;
    private String introduce;

    private AllergyInfoDto userAllergyInfo;
    private Boolean isAuthor;

    public UserProfileDto(User user, AllergyInfoDto userAllergyInfo, Boolean isAuthor) {
        this.nickname = user.getNickname();
        this.username = user.getUsername();
        this.regDate = user.getCreatedAt();
        this.profileImg = user.getProfileImg();
        this.introduce = user.getIntroduce();
        this.userAllergyInfo = userAllergyInfo;
        this.isAuthor = isAuthor;
    }
}
