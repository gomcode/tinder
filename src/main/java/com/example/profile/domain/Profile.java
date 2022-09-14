package com.example.profile.domain;


import lombok.*;

import javax.persistence.*;
import java.util.List;


@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Profile{

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @JoinColumn(name = "member_id", nullable = false)
        @ManyToOne(fetch = FetchType.LAZY)
        private Member member;

        public void updateNickname(String nickName) {
                this.member.setNickname(nickName);
        }

//        public void updateImage(Image image) {
//                this.member.setImageUrl(image.getPath());
//                this.member.setImageKey(image.getKey());
//        }

        public boolean validateMember(Member member) {
                return !this.member.equals(member);
        }


//        @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
//        private List<ChattingMember> chattingMember;

}
