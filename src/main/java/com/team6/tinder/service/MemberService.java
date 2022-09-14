package com.team6.tinder.service;

import com.team6.tinder.controller.request.LoginRequestDto;
import com.team6.tinder.controller.request.MemberRequestDto;
import com.team6.tinder.controller.request.TokenDto;
import com.team6.tinder.controller.response.MemberResponseDto;
import com.team6.tinder.controller.response.ResponseDto;
import com.team6.tinder.domain.Member;
import com.team6.tinder.jwt.TokenProvider;
import com.team6.tinder.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;
    //  private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    private final com.example.profile.service.S3UploaderService s3Uploader;


//    @Transactional
//    public ResponseDto<?> createMember(MemberRequestDto requestDto, MultipartFile multipartFile) throws IOException {
//        if (null != isPresentMember(requestDto.getLoginId())) {
//            return ResponseDto.fail("DUPLICATED_LOGINId",
//                    "이미 사용된 아이디 입니다.");
//        }
//
//        if (!requestDto.getLoginPw().equals(requestDto.getLoginPw2())) {
//            return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
//                    "비밀번호가 일치하지 않습니다.");
//        }
//
//        //이미지 s3업로드(s3UploaderService 메서드 활용) 후 값 image변수에 입력
//        Image image = s3Uploader.uploadFiles(multipartFile, "static/");
//
//        Member member = Member.builder()
//                .nickname(requestDto.getNickname())
//                .loginId(requestDto.getLoginId())
//                .loginPw(passwordEncoder.encode(requestDto.getLoginPw()))
//                .sex(requestDto.getSex())
//                .imageUrl(image.getPath())
//                .imageKey(image.getKey())
//                .build();
//        memberRepository.save(member);
//        return ResponseDto.success(
//                MemberResponseDto.builder()
//                        .memberId(member.getMemberId())
//                        .nickname(member.getNickname())
//                        .sex(member.getSex())
//                        .imageUrl(image.getPath())
//                        .build()
//        );
//    }

    @Transactional
    public ResponseDto<?> createMember(MemberRequestDto requestDto) {
        if (null != isPresentMember(requestDto.getLoginId())) {
            return ResponseDto.fail("DUPLICATED_LOGINId",
                    "이미 사용된 아이디 입니다.");
        }

        if (!requestDto.getLoginPw().equals(requestDto.getLoginPw2())) {
            return ResponseDto.fail("PASSWORDS_NOT_MATCHED",
                    "비밀번호가 일치하지 않습니다.");
        }

        Member member = Member.builder()
                .nickname(requestDto.getNickname())
                .loginId(requestDto.getLoginId())
                .loginPw(passwordEncoder.encode(requestDto.getLoginPw()))
                .sex(requestDto.getSex())
                .build();
        memberRepository.save(member);
        return ResponseDto.success(
                MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .nickname(member.getNickname())
                        .sex(member.getSex())
                        .build()
        );
    }


    @Transactional
    public ResponseDto<?> login(LoginRequestDto requestDto, HttpServletResponse response) {
        Member member = isPresentMember(requestDto.getLoginId());
        if (null == member) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "사용자를 찾을 수 없습니다.");
        }

        if (!member.validatePassword(passwordEncoder, requestDto.getLoginPw())) {
            return ResponseDto.fail("INVALID_MEMBER", "사용자를 찾을 수 없습니다.");
        }

//    UsernamePasswordAuthenticationToken authenticationToken =
//        new UsernamePasswordAuthenticationToken(requestDto.getNickname(), requestDto.getPassword());
//    Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        TokenDto tokenDto = tokenProvider.generateTokenDto(member);
        tokenToHeaders(tokenDto, response);

        return ResponseDto.success(
                MemberResponseDto.builder()
                        .memberId(member.getMemberId())
                        .loginId(member.getLoginId())
                        .nickname(member.getNickname())
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
    public Member isPresentMember(String loginId) {
        Optional<Member> optionalMember = memberRepository.findByLoginId(loginId);
        return optionalMember.orElse(null);
    }

    public void tokenToHeaders(TokenDto tokenDto, HttpServletResponse response) {
        response.addHeader("Authorization", "Bearer " + tokenDto.getAccessToken());
        response.addHeader("Refresh-Token", tokenDto.getRefreshToken());
        response.addHeader("Access-Token-Expire-Time", tokenDto.getAccessTokenExpiresIn().toString());
    }


}


