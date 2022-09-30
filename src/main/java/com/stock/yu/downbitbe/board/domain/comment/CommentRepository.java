package com.stock.yu.downbitbe.board.domain.comment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Query("select m from Comment m where m.post.postId = :postId order by COALESCE(m.parent, m.commentId), m.commentId")
    Page<Comment> findAllByPostPostId(Long postId, Pageable pageable);

    Comment findCommentByCommentId(Long commentId);
}