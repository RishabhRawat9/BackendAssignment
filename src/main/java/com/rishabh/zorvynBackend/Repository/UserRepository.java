package com.rishabh.zorvynBackend.Repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.rishabh.zorvynBackend.Entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}