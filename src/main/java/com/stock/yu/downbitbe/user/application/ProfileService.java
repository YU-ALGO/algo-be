package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.board.domain.comment.Comment;
import com.stock.yu.downbitbe.board.domain.comment.CommentRepository;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.board.domain.post.PostRepository;
import com.stock.yu.downbitbe.food.domain.AllergyInfoDto;
import com.stock.yu.downbitbe.food.domain.FoodLikeRepository;
import com.stock.yu.downbitbe.user.domain.profile.ProfileCommentDto;
import com.stock.yu.downbitbe.user.domain.profile.ProfilePostDto;
import com.stock.yu.downbitbe.user.domain.profile.UserFoodLikeResponseDto;
import com.stock.yu.downbitbe.user.domain.profile.UserProfileDto;
import com.stock.yu.downbitbe.user.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
