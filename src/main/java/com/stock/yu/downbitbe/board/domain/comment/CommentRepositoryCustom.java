package com.stock.yu.downbitbe.board.domain.comment;

import com.stock.yu.downbitbe.user.domain.profile.ProfileCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<ProfileCommentDto> findAllByUserNickname(String nickname, Pageable pageable);

}
