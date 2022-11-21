package com.stock.yu.downbitbe.user.domain.user;


import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenRepository extends JpaRepository<UserToken, String> {

}
