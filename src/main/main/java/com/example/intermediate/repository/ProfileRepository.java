package com.example.intermediate.repository;



import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileRepository extends JpaRepository<Profile, Long> {


    Optional<Profile> findByMember(Member member);

    Optional<Profile> findByMember_Nickname(String nickname);

    Optional<Profile> findByMember_Id(Long id);



}
