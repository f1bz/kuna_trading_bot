package com.tradingbot.kuna.repository;

import com.tradingbot.kuna.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    @Query("select u from User u where u.id = :userId")
    Optional<User> findByUserId(Long userId);
}