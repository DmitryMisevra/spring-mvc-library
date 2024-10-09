package ru.javacode.springmvclibrary.user.service;


import org.springframework.security.oauth2.core.user.OAuth2User;
import ru.javacode.springmvclibrary.user.Dto.UserProfileDto;
import ru.javacode.springmvclibrary.user.model.User;

import java.util.Optional;

public interface UserService {
    Optional<User> findByEmail(String email);
    User save(User user);
    UserProfileDto buildUserProfile(OAuth2User principal);
}

