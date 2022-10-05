package com.stock.yu.downbitbe.board.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select m from Comment m where m.post.postId = :postId order by COALESCE(m.parent, m.commentId), m.commentId")
    Page<Comment> findAllByPostPostId(@Param(value = "postId") Long postId, Pageable pageable);

    Comment findCommentByCommentId(Long commentId);

    Boolean existsCommentByParent(Long parent);

    @Modifying(clearAutomatically = true)
    @Query("update Comment set content = :content, isDeleted = true, user = -1 where commentId = :commentId")
    int deleteCommentView(@Param(value = "commentId") Long commentId, @Param(value = "content") String content);

    @Query("select count(c) from Comment c where c.parent = :parentId")
    Long countCommentByParent(@Param(value = "parentId") Long parentId);

    @Modifying
    @Query("delete from Comment c where c.parent = :id or c.commentId = :id")
    Long deleteCommentsByCommentIdAndParent(@Param(value = "id")Long parent);
}