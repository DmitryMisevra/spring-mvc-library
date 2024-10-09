package ru.javacode.springmvclibrary.user.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.javacode.springmvclibrary.user.Dto.UserProfileDto;
import ru.javacode.springmvclibrary.user.service.UserService;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<UserProfileDto> getUserProfile(@AuthenticationPrincipal OAuth2User principal) {
        UserProfileDto userProfile = userService.buildUserProfile(principal);
        if (userProfile == null) {
            return ResponseEntity.status(401).build();
        }
        return ResponseEntity.ok(userProfile);
    }
}
