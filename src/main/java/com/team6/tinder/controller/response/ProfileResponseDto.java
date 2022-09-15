package com.team6.tinder.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Long memberId;
    private String nickname;
    private String sex;
//    private String imageUrl;
//    private List<ChattingMember> chattingMemberList;
}
