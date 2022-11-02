package com.stock.yu.downbitbe.board.infra;


import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.stock.yu.downbitbe.board.domain.post.Post;
import com.stock.yu.downbitbe.board.domain.post.PostListResponseDto;
import com.stock.yu.downbitbe.board.domain.post.PostRepositoryCustom;
import com.stock.yu.downbitbe.board.domain.post.PostSearchType;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.persistence.EntityManager;

import java.util.ArrayList;
import java.util.List;

import static com.stock.yu.downbitbe.board.domain.board.QBoard.board;
import static com.stock.yu.downbitbe.board.domain.post.QPost.post;
import static org.springframework.util.StringUtils.hasText;

@Log4j2
public class PostRepositoryImpl implements PostRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(EntityManager em){
        this.queryFactory = new JPAQueryFactory(em);
    }

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

    BooleanExpression isSearchable(String keyword, PostSearchType searchType){
        if(!hasText(keyword) || searchType == null)
            return null;

        if(searchType == PostSearchType.TITLE)
            return post.title.contains(keyword);
        else if(searchType == PostSearchType.AUTHOR)
            return post.user.username.eq(keyword);
        else
            return post.title.contains(keyword).or(post.user.username.eq(keyword));
    }

}
