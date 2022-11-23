package com.algo.yu.algobe.board.infra;

import com.algo.yu.algobe.board.domain.board.QBoard;
import com.algo.yu.algobe.board.domain.post.PostRepositoryCustom;
import com.algo.yu.algobe.board.domain.post.PostSearchType;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.algo.yu.algobe.board.domain.post.Post;
import com.algo.yu.algobe.board.domain.post.PostListResponseDto;
import com.algo.yu.algobe.user.domain.profile.ProfilePostDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static com.algo.yu.algobe.board.domain.board.QBoard.board;
import static com.algo.yu.algobe.board.domain.post.QPost.post;
import static org.springframework.util.StringUtils.hasText;

@Log4j2
@Repository
@RequiredArgsConstructor
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    private List<OrderSpecifier> getOrderSpecifier(Sort sort){
        List<OrderSpecifier> orders = new ArrayList<>();

        sort.stream().forEach(order -> {
            Order direction = order.isAscending() ? Order.ASC : Order.DESC;
            String prop = order.getProperty();
            PathBuilder orderByExpression = new PathBuilder(Post.class, "post");
            orders.add(new OrderSpecifier(direction, orderByExpression.get(prop)));
            orders.add(new OrderSpecifier(Order.DESC, post.createdAt));
        });
        return orders;
    }

    @Override
    public Page<PostListResponseDto> findAllByBoardBoardId(Long boardId, Pageable pageable, String keyword, PostSearchType searchType) {
        List<PostListResponseDto> results = queryFactory
                .select(Projections.constructor(PostListResponseDto.class,
                        post.postId,
                        post.title,
                        post.user.nickname.as("author"),
                        post.board.boardId,
                        post.likeCount,
                        post.commentCount,
                        post.viewCount,
                        post.createdAt
                ))
                .from(post)
                .innerJoin(post.board, board)
                .where(post.board.boardId.eq(boardId),
                        isSearchable(keyword, searchType))
                .orderBy(getOrderSpecifier(pageable.getSort()).toArray(OrderSpecifier[]::new))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(post)
                .where(post.board.boardId.eq(boardId),
                        isSearchable(keyword, searchType))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    @Override
    public List<PostListResponseDto> findTopPosts(int size, LocalDateTime start, LocalDateTime now) {
        List<PostListResponseDto> results = queryFactory
                .select(Projections.constructor(PostListResponseDto.class,
                        post.postId,
                        post.title,
                        post.user.nickname.as("author"),
                        post.board.boardId,
                        post.likeCount,
                        post.commentCount,
                        post.viewCount,
                        post.createdAt
                ))
                .from(post)
                .innerJoin(post.board, board)
                .where(post.createdAt.between(start, now)
                        .and(post.board.boardId.ne(1L)))
                .orderBy(post.likeCount.desc(), post.viewCount.desc())
                .limit(size)
                .fetch();

        return results;
    }

    @Override
    public Page<ProfilePostDto> findAllByUserNickname(String nickname, Pageable pageable) {
        List<ProfilePostDto> results = queryFactory
                .select(Projections.constructor(ProfilePostDto.class,
                        post.board.boardId,
                        post.postId,
                        post.title,
                        post.createdAt
                ))
                .from(post)
                .innerJoin(post.board, board)
                .where(post.user.nickname.eq(nickname))
                .orderBy(post.createdAt.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        int totalSize = queryFactory
                .selectFrom(post)
                .where(post.user.nickname.eq(nickname))
                .fetch().size();

        return new PageImpl<>(results, pageable, totalSize);
    }

    BooleanExpression isSearchable(String keyword, PostSearchType searchType){
        if(!hasText(keyword) || searchType == null)
            return null;

        if(searchType == PostSearchType.TITLE)
            return post.title.contains(keyword);
        else if(searchType == PostSearchType.AUTHOR)
            return post.user.nickname.eq(keyword);
        else
            return post.title.contains(keyword).or(post.user.nickname.eq(keyword));
    }

}
