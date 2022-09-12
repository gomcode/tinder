package com.example.profile.service;

import com.example.profile.controller.response.ProfileResponseDto;
import com.example.profile.controller.response.ResponseDto;
import com.example.profile.domain.Member;
import com.example.profile.domain.Profile;
import com.example.profile.jwt.TokenProvider;
import com.example.profile.repository.ProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProfileService {


    private final ProfileRepository profileRepository;

    private final TokenProvider tokenProvider;

    private final S3Upload s3Upload;


    @Transactional(readOnly = true)
    public ResponseDto<?> getProfile(Long memberId) {
        Profile profile = isPresentProfileId(memberId);
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .memberId(profile.getMember().getMemberId())
                        .nickname(profile.getMember().getNickname())
                        .sex(profile.getMember().getSex())
                        .img(profile.getMember().getImage())
//                        .chattingMemberList()
                        .build()
        );
    }

    @Transactional(readOnly = true)
    public ResponseDto<?> getMyProfile() {
        Profile profile = isPresentProfile();
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }

        return ResponseDto.success(
                ProfileResponseDto.builder()
                        .memberId(profile.getMember().getMemberId())
                        .nickname(profile.getMember().getNickname())
                        .sex(profile.getMember().getSex())
                        .img(profile.getMember().getImage())
//                        .chattingMemberList()
                        .build()
        );
    }


    @Transactional
    public ResponseDto<String> updateImage( MultipartFile multipartFile, String fileSize, HttpServletRequest request) throws IOException {

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

        Profile profile = isPresentProfile();
        if (null != profile) {
            if (profile.validateMember(member)) {
                return ResponseDto.fail("BAD_REQUEST", "작성자만 이미지를 등록 할 수 있습니다.");
            } else {

                String imageUrl = s3Upload.upload(multipartFile.getInputStream(), multipartFile.getOriginalFilename(), fileSize);
                profile.updateImage(imageUrl);
                return ResponseDto.success("이미지 등록 완료" + imageUrl);
            }

        } else {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }
    }

    @Transactional
    public ResponseDto<Profile> updateNickname(String nickname, HttpServletRequest request) {
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

        Profile profile = isPresentProfile();
        if (null == profile) {
            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
        }

        if (profile.validateMember(member)) {
            return ResponseDto.fail("BAD_REQUEST", "작성자만 수정할 수 있습니다.");
        }

        profile.updateNickname(nickname);
        return ResponseDto.success(profile);
    }

    @Transactional(readOnly = true)
    public Profile isPresentProfile(){
        Member member = tokenProvider.getMemberFromAuthentication();

        Optional<Profile> optionalProfile = profileRepository.findByMember(member);
        return optionalProfile.orElse(null);
    }


    @Transactional(readOnly = true)
    public Profile isPresentProfileId(Long memberId) {
        Optional<Profile> optionalProfile = profileRepository.findById(memberId);
        return optionalProfile.orElse(null);
    }

    @Transactional
    public Member validateMember (HttpServletRequest request){
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
