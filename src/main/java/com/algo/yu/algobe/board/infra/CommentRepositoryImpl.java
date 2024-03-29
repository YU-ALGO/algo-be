package com.algo.yu.algobe.board.infra;

import com.algo.yu.algobe.board.domain.board.QBoard;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.algo.yu.algobe.board.domain.comment.CommentRepositoryCustom;
import com.algo.yu.algobe.user.domain.profile.ProfileCommentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.algo.yu.algobe.board.domain.post.QPost.post;
import static com.algo.yu.algobe.board.domain.comment.QComment.comment;

@Repository
@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<ProfileCommentDto> findAllByUserNickname(String nickname, Pageable pageable) {
        List<ProfileCommentDto> results = queryFactory
                .select(Projections.constructor(ProfileCommentDto.class,
                        comment.post.board.boardId,
                        comment.post.postId,
                        comment.content,
                        comment.createdAt
                ))
                .from(comment)
                .innerJoin(comment.post, post)
                .innerJoin(comment.post.board, QBoard.board)
                .where(comment.user.nickname.eq(nickname))
                .orderBy(comment.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(comment)
                .where(comment.user.nickname.eq(nickname))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }
}
