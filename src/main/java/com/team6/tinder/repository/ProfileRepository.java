package com.team6.tinder.repository;


import com.team6.tinder.domain.Member;
import com.team6.tinder.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {


    Optional<Profile> findByMember(Member member);
}
