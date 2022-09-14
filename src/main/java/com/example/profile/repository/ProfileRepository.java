package com.example.profile.repository;


import com.example.profile.domain.Member;
import com.example.profile.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {


    Optional<Profile> findByMember(Member member);
}
