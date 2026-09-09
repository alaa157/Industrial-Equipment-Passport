package com.industrial.auth.repository;

import com.industrial.auth.entity.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,UUID> {
Optional<User> findByUsernameIgnoreCaseOrEmailIgnoreCase(String username,String email);
boolean existsByUsernameIgnoreCase(String username);
boolean existsByEmailIgnoreCase(String email);
}
