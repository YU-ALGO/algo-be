package com.stock.yu.downbitbe.board.domain.post;

import com.stock.yu.downbitbe.user.domain.profile.ProfilePostDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepositoryCustom {
    Page<PostListResponseDto> findAllByBoardBoardId(Long boardId, Pageable pageable, String keyword, PostSearchType searchType);

    List<PostListResponseDto> findTopPosts(int size, LocalDateTime start, LocalDateTime now);

    Page<ProfilePostDto> findAllByUserNickname(String nickname, Pageable pageable);
}