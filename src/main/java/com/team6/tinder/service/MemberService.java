package com.team6.tinder.service;

import com.team6.tinder.controller.request.LoginRequestDto;
import com.team6.tinder.controller.request.MemberRequestDto;
import com.team6.tinder.controller.request.TokenDto;
import com.team6.tinder.controller.response.MemberResponseDto;
import com.team6.tinder.controller.response.ResponseDto;
import com.team6.tinder.domain.Member;
import com.team6.tinder.jwt.TokenProvider;
import com.team6.tinder.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;


@AllArgsConstructor
@Service
public class MemberService {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public Member isPresentLoginId(String loginId) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        return optionalMember.orElse(null);
    }

    @Transactional(readOnly = true)
    public Member isPresentNickname(String nickname) {
        Optional<Member> optionalMember = memberRepository.findByNickname(nickname);
        return optionalMember.orElse(null);
    }

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentLoginId(requestDto.getLoginId())) {
            return ResponseDto.fail("DUPLICATED_LOGINID",
                    "이미 사용된 아이디 입니다.");
        }


        if (null != isPresentNickname(requestDto.getNickname())) {
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
                        .memberId(member.getMemberId())
                        .nickname(member.getNickname())
                        .sex(member.getSex())
                        //.img()
                        .build()
        );
    }

    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentLoginId(requestDto.getLoginId());
        if (null == member) {
            return ResponseDto.fail("LOGINID_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getLoginPw())) {
            return ResponseDto.fail("INVALID_LOGINPW", "사용자를 찾을 수 없습니다.");
        }

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .loginId(member.getLoginId())
                        .nickname(member.getNickname())
                        .sex(member.getSex())
                        .build()
        );
    }




    public ResponseDto<?> logout(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }
        Member member = tokenProvider.getMemberFromAuthentication();
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND", "사용자를 찾을 수 없습니다.");
        }
        return tokenProvider.deleteRefreshToken(member);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }

//
//    @Transactional(readOnly = true)
//    public Member isPresentMember(String loginId) {
//        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
//        return optionalMember.orElse(null);
//    }


}


