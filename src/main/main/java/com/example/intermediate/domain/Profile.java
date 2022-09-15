package com.example.intermediate.domain;


import com.example.intermediate.controller.request.ProfileRequestDto;
import lombok.*;

import javax.persistence.*;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JoinColumn(name = "member_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY)
        private Member member;

        public void updateSex(ProfileRequestDto sex) {
                this.member.setSex(sex.getSex());
        }

        public boolean validateMember(Member member) {
                return !this.member.equals(member);
        }

}
