package com.stock.yu.downbitbe.user.domain.user;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomUserRepository extends JpaRepository<User, String> {

//    @EntityGraph(attributePaths = {"gradeSet"}, type = EntityGraph.EntityGraphType.LOAD)
//    @Query("SELECT u FROM User u WHERE u.type= :type AND u.userId= :userId")
//    Optional<User> findByUserIdAndType(@Param("userId") String userId, @Param("type") LoginType type);

    Optional<User> findByUsernameAndLoginType(String username, LoginType type);

    User findByUsername(String username);

    User findByNickname(String nickname);

    Boolean existsByUsername(String username);

    Boolean existsByNickname(String nickname);



    /*@EntityGraph(attributePaths = {"gradeSet"}, type = EntityGraph.EntityGraphType.LOAD)
    List<User> findByUserIdAndType(String userId, LoginType type);*/
}