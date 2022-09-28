package com.stock.yu.downbitbe.board.domain.post;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostLikeRepository extends JpaRepository<PostLike, Long> {
    Boolean existsByPostPostIdAndUserUserId(Long postId, Long userId);
    Optional<PostLike> findPostLikeByPostPostIdAndUserUserId(Long postId, Long userId);
}
