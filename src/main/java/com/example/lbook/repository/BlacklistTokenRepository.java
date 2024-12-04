package com.example.lbook.repository;


import com.example.lbook.entity.BlacklistToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlacklistTokenRepository extends JpaRepository<BlacklistToken, Long> {
    Optional<BlacklistToken> findByToken(String token);
    boolean existsByToken(String token);
}
