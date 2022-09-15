package com.team6.tinder.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import java.util.Objects;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Member {

    @Id
//    @Column(name ="member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    private String loginId;

    @Column(nullable = false)
    private String sex;

    @Column(nullable = false)
    @JsonIgnore
    private String loginPw;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) {
            return false;
        }
        Member member = (Member) o;
        return memberId != null && Objects.equals(memberId, member.memberId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public boolean validatePassword(PasswordEncoder passwordEncoder, String loginPw) {
        return passwordEncoder.matches(loginPw, this.loginPw);
    }
}