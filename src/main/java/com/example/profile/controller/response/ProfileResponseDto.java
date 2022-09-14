package com.example.profile.controller.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponseDto {
    private Long memberId;
    private String nickname;
    private Long sex;
//    private String imageUrl;
//    private List<ChattingMember> chattingMemberList;
}
