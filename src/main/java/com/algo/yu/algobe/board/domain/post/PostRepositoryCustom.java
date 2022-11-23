package com.algo.yu.algobe.board.domain.post;

import com.algo.yu.algobe.user.domain.profile.ProfilePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    Page<PostListResponseDto> findAllByBoardBoardId(Long boardId, Pageable pageable, String keyword, PostSearchType searchType);

    List<PostListResponseDto> findTopPosts(int size, LocalDateTime start, LocalDateTime now);

    Page<ProfilePostDto> findAllByUserNickname(String nickname, Pageable pageable);
}