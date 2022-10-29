package com.stock.yu.downbitbe.user.application;

import com.stock.yu.downbitbe.board.domain.comment.Comment;
import com.stock.yu.downbitbe.board.domain.comment.CommentDto;
import com.stock.yu.downbitbe.board.domain.comment.CommentRepository;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.board.domain.post.PostRepository;
import com.stock.yu.downbitbe.user.domain.profile.ProfileCommentDto;
import com.stock.yu.downbitbe.user.domain.profile.ProfilePostDto;
import com.stock.yu.downbitbe.user.domain.profile.UserProfileDto;
import com.stock.yu.downbitbe.user.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final CustomUserRepository userRepository;
    private final UserAllergyInfoRepository userAllergyInfoRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    @Transactional(readOnly = true)
    public UserProfileDto getUserProfileByNickname(String nickname, boolean isAuthor) {
        User user = userRepository.findByNickname(nickname);
        UserAllergyInfo allergyInfo = userAllergyInfoRepository.findByUserId(user.getUserId());

        return new UserProfileDto(user, allergyInfo.toMap(), isAuthor);
    }

    @Transactional(readOnly = true)
    public List<ProfileCommentDto> getUserCommentsByNickname(String nickname) {
        List<Comment> commentList = commentRepository.findAllByUserNickname(nickname);

        return commentList.stream().map(comment ->
                new ProfileCommentDto(comment, comment.getPost().getBoard().getBoardId(), comment.getPost().getPostId())).
                collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProfilePostDto> getUserPostsByNickname(String nickname) {
        List<Post> commentList = postRepository.findAllByUserNickname(nickname);

        return commentList.stream().map(post ->
                        new ProfilePostDto(post, post.getBoard().getBoardId(), post.getPostId())).
                collect(Collectors.toList());
    }
}
