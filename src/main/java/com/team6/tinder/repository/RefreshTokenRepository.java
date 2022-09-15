package com.team6.tinder.repository;

import com.team6.tinder.domain.Member;
import com.team6.tinder.domain.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByMemberId(Member member);
}
