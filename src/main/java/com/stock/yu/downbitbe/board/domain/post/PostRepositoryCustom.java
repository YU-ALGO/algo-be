package com.stock.yu.downbitbe.board.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostRepositoryCustom {
    Page<PostListResponseDto> findAllByBoardBoardId(Long boardId, Pageable pageable, String keyword, PostSearchType searchType);
}