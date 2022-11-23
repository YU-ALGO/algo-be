package com.stock.yu.downbitbe.board.domain.comment;

import com.stock.yu.downbitbe.user.domain.profile.ProfileCommentDto;
import com.stock.yu.downbitbe.user.domain.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom{
    @Query("select m from Comment m where m.post.postId = :postId order by COALESCE(m.parent, m.commentId), m.commentId")
    Page<Comment> findAllByPostPostId(@Param(value = "postId") Long postId, Pageable pageable);

    Comment findCommentByCommentId(Long commentId);

    Boolean existsCommentByParent(Long parent);


    // 삭제된 사용자입니다. 항상 1번 유저로 추가하기
    @Modifying(clearAutomatically = true)
    @Query("update Comment set content = :content, isDeleted = true where commentId = :commentId")
    int deleteCommentView(@Param(value = "commentId") Long commentId, @Param(value = "content") String content);

    @Query("select count(c) from Comment c where c.parent = :parentId")
    Long countCommentByParent(@Param(value = "parentId") Long parentId);

    @Modifying(clearAutomatically = true)
    @Query("delete from Comment c where c.parent = :id or c.commentId = :id")
    int deleteCommentsByCommentIdAndParent(@Param(value = "id")Long parent);
}