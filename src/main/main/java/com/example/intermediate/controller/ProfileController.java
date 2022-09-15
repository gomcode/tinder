package com.example.intermediate.controller;

import com.example.intermediate.controller.request.ProfileRequestDto;
import com.example.intermediate.controller.response.ResponseDto;
import com.example.intermediate.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;


@RequiredArgsConstructor
@RestController
public class ProfileController {

    private final ProfileService profileService;

    @RequestMapping(value = "/profile/member/{memberId}", method = RequestMethod.GET)
    public ResponseDto<?> getProfile(@PathVariable Long memberId, HttpServletRequest request) {
        return profileService.getProfile(memberId, request);
    }



    @RequestMapping(value = "/profile/member/mypage/{nickname}", method = RequestMethod.GET)
    public ResponseDto<?> getMyProfile(@PathVariable String nickname, HttpServletRequest request) {
        return profileService.getMyProfile(nickname, request);
    }



    @RequestMapping(value = "/profile/update/sex/{nickname}", method = RequestMethod.PUT)
    public ResponseDto<?> updateSex(@PathVariable String nickname, @RequestBody ProfileRequestDto sex, HttpServletRequest request) {
        return profileService.updateSex(nickname, sex, request);
    }

}
