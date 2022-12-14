package com.team6.tinder.service;

import com.team6.tinder.controller.response.ProfileResponseDto;
import com.team6.tinder.controller.response.ResponseDto;
import com.team6.tinder.domain.Member;
import com.team6.tinder.domain.Profile;
import com.team6.tinder.jwt.TokenProvider;
import com.team6.tinder.repository.ProfileRepository;
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

    private final com.team6.tinder.service.S3UploaderService s3Uploader;


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
//                        .imageUrl(profile.getMember().getImageUrl())
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
//                        .imageUrl(profile.getMember().getImageUrl())
//                        .chattingMemberList()
                        .build()
        );
    }


//    @Transactional
//    public ResponseDto<String> updateImage(MultipartFile multipartFile, HttpServletRequest request) throws IOException {
//
//        if (null == request.getHeader("Refresh-Token")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        if (null == request.getHeader("Authorization")) {
//            return ResponseDto.fail("MEMBER_NOT_FOUND",
//                    "로그인이 필요합니다.");
//        }
//
//        Member member = validateMember(request);
//        if (null == member) {
//            return ResponseDto.fail("INVALID_TOKEN", "Token이 유효하지 않습니다.");
//        }
//
//        Profile profile = isPresentProfile();
//        if (null != profile) {
//            if (profile.validateMember(member)) {
//                return ResponseDto.fail("BAD_REQUEST", "작성자만 이미지를 등록 할 수 있습니다.");
//            } else {
//
//                //이미지 업로드 및 해당 값 image 주입
//                Image image = s3Uploader.uploadFiles(multipartFile, "static/");
//
//                //기존 게시글에 연결되었던 image를 imagekey값을 통해 찾아서 제거
//                s3Uploader.remove(profile.getMember().getImageKey());
//
//                //바뀐 내용 업데이트
//                profile.updateImage(image);
//                return ResponseDto.success("이미지 등록 완료" + image);
//            }
//
//        } else {
//            return ResponseDto.fail("NOT_FOUND", "존재하지 않는 프로필 입니다.");
//        }
//    }

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
    public Profile isPresentProfile() {
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
    public Member validateMember(HttpServletRequest request) {
        if (!tokenProvider.validateToken(request.getHeader("Refresh-Token"))) {
            return null;
        }
        return tokenProvider.getMemberFromAuthentication();
    }


}
