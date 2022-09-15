package com.example.intermediate.service;

import com.example.intermediate.controller.request.ProfileRequestDto;
import com.example.intermediate.controller.response.ProfileResponseDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.domain.Member;
import com.example.intermediate.domain.Profile;
import com.example.intermediate.jwt.TokenProvider;
import com.example.intermediate.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepository profileRepository;

    private final TokenProvider tokenProvider;


    @Transactional(readOnly = true)
    public ResponseDto<?> getProfile(Long id, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Profile profile = isPresentProfileId(id);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .id(profile.getMember().getId())
                        .loginId(profile.getMember().getLoginId())
                        .nickname(profile.getMember().getNickname())
                        .sex(profile.getMember().getSex())
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getMyProfile(String nickname, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Profile profile = isPresentProfile(nickname);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .id(profile.getMember().getId())
                        .loginId(profile.getMember().getLoginId())
                        .nickname(profile.getMember().getNickname())
                        .sex(profile.getMember().getSex())
                        .build()
        );
    }



    @Transactional
    public ResponseDto<Profile> updateSex(String nickname, ProfileRequestDto sex, HttpServletRequest request) {
        if (null == request.getHeader("Refresh-Token")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        if (null == request.getHeader("Authorization")) {
            return ResponseDto.fail("MEMBER_NOT_FOUND",
                    "로그인이 필요합니다.");
        }

        Member member = validateMember(request);
        if (null == member) {
            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
        }

        Profile profile = isPresentProfile(nickname);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }

        if (profile.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        profile.updateSex(sex);
        return ResponseDto.success(profile);
    }

    @Transactional(readOnly = true)
    public Profile isPresentProfile(String nickname) {
        Optional<Profile> optionalProfile = profileRepository.findByMember_Nickname(nickname);
        return optionalProfile.orElse(null);
    }

    @Transactional(readOnly = true)
    public Profile isPresentProfileId(Long id) {
        Optional<Profile> optionalProfile = profileRepository.findByMember_Id(id);
        return optionalProfile.orElse(null);
    }


    @Transactional
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
