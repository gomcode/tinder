package com.team6.tinder.controller;

import com.team6.tinder.controller.request.LoginRequestDto;
import com.team6.tinder.controller.request.MemberRequestDto;
import com.team6.tinder.controller.response.ResponseDto;
import com.team6.tinder.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

@Slf4j
@Controller
@RequiredArgsConstructor
@RestController
public class MemberController {

//    private final MemberService memberService;
//    @PostMapping(value = "/member/signup", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
//    public ResponseDto<?> signup(@RequestPart("key") MemberRequestDto requestDto, @RequestPart(value = "file",required = false) MultipartFile multipartFile) throws IOException {
//        return memberService.createMember(requestDto, multipartFile);
//    }

    private final MemberService memberService;

    @RequestMapping(value = "/member/signup", method = RequestMethod.POST)
    public ResponseDto<?> signup(@RequestBody @Valid MemberRequestDto requestDto){
        return memberService.createMember(requestDto);
    }


    @RequestMapping(value = "/member/login", method = RequestMethod.POST)
    public ResponseDto<?> login(@RequestBody @Valid LoginRequestDto requestDto,
                                HttpServletResponse response
    ) {
        return memberService.login(requestDto, response);
    }

    @RequestMapping(value = "/member/logout", method = RequestMethod.POST)
    public ResponseDto<?> logout(HttpServletRequest request) {
        return memberService.logout(request);
    }
}
