package com.example.profile.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MemberResponseDto {
    private Long memberId;
    private String loginId;
    private String loginPw;
    private String loginPw2;
    private String nickname;
    private Long sex;
//    private String imageUrl;
}