package com.team6.tinder.repository;

import com.team6.tinder.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findById(Long id);
    Optional<Member> findByNickname(String nickname);
    Optional<Member> findByLoginId(String loginId);
}