package com.algo.yu.algobe.user.application;

import com.algo.yu.algobe.board.domain.comment.CommentRepository;
import com.algo.yu.algobe.board.domain.post.PostRepository;
import com.algo.yu.algobe.user.domain.profile.ProfileCommentDto;
import com.algo.yu.algobe.user.domain.profile.ProfilePostDto;
import com.algo.yu.algobe.user.domain.profile.UserFoodLikeResponseDto;
import com.algo.yu.algobe.user.domain.profile.UserProfileDto;
import com.algo.yu.algobe.user.domain.user.CustomUserRepository;
import com.algo.yu.algobe.user.domain.user.User;
import com.algo.yu.algobe.user.domain.user.UserAllergyInfoRepository;
import com.algo.yu.algobe.food.domain.AllergyInfoDto;
import com.algo.yu.algobe.food.domain.FoodLikeRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CustomUserRepository userRepository;
    private final UserAllergyInfoRepository userAllergyInfoRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final FoodLikeRepository foodLikeRepository;
    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileByNickname(String nickname, boolean isAuthor) {
        User user = userRepository.findByNickname(nickname);
        AllergyInfoDto allergyInfo = userAllergyInfoRepository.findAllergyDtoByUserId(user.getUserId());

        return new UserProfileDto(user, allergyInfo, isAuthor);
    }

    @Transactional(readOnly = true)
    public Page<ProfileCommentDto> getUserCommentsByNickname(String nickname, Pageable pageable) {
        return commentRepository.findAllByUserNickname(nickname, pageable);
    }

    @Transactional(readOnly = true)
    public Page<ProfilePostDto> getUserPostsByNickname(String nickname, Pageable pageable) {
        return postRepository.findAllByUserNickname(nickname, pageable);
    }

    @Transactional(readOnly = true)
    public AllergyInfoDto getUserAllergyByNickname(String nickname) {
        User user = userRepository.findByNickname(nickname);
        return userAllergyInfoRepository.findAllergyDtoByUserId(user.getUserId());
    }

    @Transactional(readOnly = true)
    public Page<UserFoodLikeResponseDto> getUserFoodLikeListByNickname(String nickname, Pageable pageable) {
        return foodLikeRepository.getUserFoodLikeListByNickname(nickname, pageable);
    }

}
