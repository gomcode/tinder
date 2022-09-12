package com.team6.tinder.controller.request;

import javax.validation.constraints.NotBlank;

public class LoginRequestDto {
    @NotBlank
    private String loginId;

    @NotBlank
    private String loginPw;
}
