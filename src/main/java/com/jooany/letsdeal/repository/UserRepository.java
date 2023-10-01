package com.jooany.letsdeal.repository;

import com.jooany.letsdeal.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUserName(String userName);
    Optional<User> findByNickname(String nickname);
    void deleteByUserName(String userName);

}