package com.team6.tinder.controller.response;

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
    private String loginId; //기존 id에서 변경함
    private String nickname; //기존 name에서 변경함
    private String sex;
    //private String img;
    //private ??? category; 자료형을 어떻게 받아와야할까?
}

