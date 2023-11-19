package com.banu.repository;

import com.banu.repository.entity.Auth;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthServiceRepository extends JpaRepository<Auth,Long> {
    Optional<Auth> findOptionalByUsernameAndPassword(String username, String password);
}
