package com.sedef.blogum.repository;

import com.sedef.blogum.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {
    Optional<User> findByUserName(String username);

    @Transactional
    @Modifying
    @Query("UPDATE User u SET u.isEnabled = TRUE WHERE u.userName = ?1")
    int  enableAppUser(String email);
}
