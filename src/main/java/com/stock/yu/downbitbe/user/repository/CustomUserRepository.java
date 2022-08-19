package com.stock.yu.downbitbe.user.repository;

import com.stock.yu.downbitbe.user.entity.LoginType;
import com.stock.yu.downbitbe.user.entity.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<User, String> {

    @EntityGraph(attributePaths = {"gradeSet"}, type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT u FROM User u WHERE u.type= :type AND u.userId= :userId")
    Optional<User> findByUserIdAndType(@Param("userId") String userId, @Param("type") LoginType type);

    /*@EntityGraph(attributePaths = {"gradeSet"}, type = EntityGraph.EntityGraphType.LOAD)
    List<User> findByUserIdAndType(String userId, LoginType type);*/

}
