package com.algo.yu.algobe.board.domain.comment;

import com.algo.yu.algobe.user.domain.profile.ProfileCommentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentRepositoryCustom {
    Page<ProfileCommentDto> findAllByUserNickname(String nickname, Pageable pageable);

}
