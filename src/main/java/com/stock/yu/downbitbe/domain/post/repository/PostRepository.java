package com.stock.yu.downbitbe.domain.post.repository;

import com.stock.yu.downbitbe.domain.post.entity.Post;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    @EntityGraph(attributePaths = "author", type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Post p where p.id =:id")
    Optional<Post> getWithAuthor(@Param("id") Long id);

    @EntityGraph(attributePaths = {"author"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("select p from Post p where p.author.userId = :userId")
    List<Post> getList(@Param("userId") String userId);

}
