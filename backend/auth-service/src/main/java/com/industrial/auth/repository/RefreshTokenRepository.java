package com.industrial.auth.repository;

import com.industrial.auth.entity.RefreshToken;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken,UUID> {
Optional<RefreshToken> findByTokenHash(String tokenHash);
}
