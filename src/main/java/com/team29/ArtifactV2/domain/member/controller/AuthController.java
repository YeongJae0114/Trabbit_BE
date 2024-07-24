package com.team29.ArtifactV2.domain.member.controller;

import com.team29.ArtifactV2.domain.member.dto.LoginDto;
import com.team29.ArtifactV2.domain.member.dto.ResponseDto;
import com.team29.ArtifactV2.domain.member.dto.SignUpDto;
import com.team29.ArtifactV2.domain.member.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class AuthController {
    private final AuthService authService;
    @PostMapping("/signUp")
    public ResponseDto<?> signUp(@RequestBody SignUpDto requestBody) {
        return authService.signUp(requestBody);
    }

    @PostMapping("/login")
    public ResponseDto<?> login(@RequestBody LoginDto requestBody) {
        return authService.login(requestBody);
    }
}
