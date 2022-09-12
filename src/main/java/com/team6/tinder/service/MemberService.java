package com.team6.tinder.service;

import com.team6.tinder.controller.request.MemberRequestDto;
import com.team6.tinder.controller.response.MemberResponseDto;
import com.team6.tinder.controller.response.ResponseDto;
import com.team6.tinder.domain.Member;
import com.team6.tinder.repository.MemberRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;



    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getNickname())) {
            return ResponseDto.fail("DUPLICATED_NICKNAME",
                    "이미 사용된 닉네임 입니다.");
        }

        if (!requestDto.getLoginPw().equals(requestDto.getLoginPw2())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
                    "비밀번호가 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .loginId(requestDto.getLoginId())
                .loginPw(passwordEncoder.encode(requestDto.getLoginPw()))
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .memberId(member.getMember_id())
                        .nickname(member.getNickname())
                        .sex(member.getSex())
                        //.img()
                        .build()
        );
    }


    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        return tokenProvider.deleteRefreshToken(member);
    }


    @Transactional(readOnly = true)
    public Member isPresentMember(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }


}


