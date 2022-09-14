package com.team6.tinder.controller;

import com.team6.tinder.controller.response.ResponseDto;
import com.team6.tinder.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @RequestMapping(value = "/profile/member/{memberId}", method = RequestMethod.GET)
    public ResponseDto<?> getProfile(@PathVariable Long memberId) {
        return profileService.getProfile(memberId);
    }

    @RequestMapping(value = "/profile/member/mypage", method = RequestMethod.GET)
    public ResponseDto<?> getMyProfile() {
        return profileService.getMyProfile();
    }


    @RequestMapping(value = "/profile/update/nickname", method = RequestMethod.PUT)
    public ResponseDto<?> updateNickname(@RequestBody String nickname, HttpServletRequest request) {
        return profileService.updateNickname(nickname, request);
    }

//    @PutMapping(value = "/profile/update/img")
//    public ResponseDto<?> updateImage(@RequestPart(value = "image") MultipartFile multipartFile, HttpServletRequest request) throws IOException {
//
//        return profileService.updateImage(multipartFile,request);
//    }

}
