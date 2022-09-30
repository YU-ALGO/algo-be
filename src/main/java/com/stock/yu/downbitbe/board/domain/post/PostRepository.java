package com.stock.yu.downbitbe.board.domain.post;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PostRepository extends JpaRepository<Post, Long> {
    Page<Post> findAllByBoardBoardId(Long boardId, Pageable pageable);

    Post findPostByPostId(Long postId);

    @Modifying(clearAutomatically = true)
    @Query("update Post set viewCount = viewCount + 1 where postId = :postId")
    int updateViewCount(@Param(value = "postId") Long postId);

    @Modifying(clearAutomatically = true)
    @Query("update Post set commentCount = commentCount + :symbol where postId = :postId")
    int updateCommentCount(@Param(value = "postId") Long postId, @Param(value = "symbol") Integer symbol);

    @Modifying(clearAutomatically = true)
    @Query("update Post set likeCount = likeCount + :symbol where postId = :postId")
    int updateLikeCount(@Param(value = "postId") Long postId, @Param(value = "symbol") Integer symbol);
}
