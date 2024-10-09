package ru.javacode.springmvclibrary.user.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import ru.javacode.springmvclibrary.security.CustomOAuth2User;
import ru.javacode.springmvclibrary.user.Dto.UserProfileDto;
import ru.javacode.springmvclibrary.user.model.Role;
import ru.javacode.springmvclibrary.user.model.User;
import ru.javacode.springmvclibrary.user.repository.UserRepository;


import java.util.Collections;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService, OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public User save(User user) {
        return userRepository.save(user);
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate =
                new org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService();

        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // Получение атрибутов пользователя
        String email = oAuth2User.getAttribute("email");
        String name = oAuth2User.getAttribute("name");

        // Поиск пользователя по email
        Optional<User> userOptional = userRepository.findByEmail(email);
        User user;

        if (userOptional.isPresent()) {
            user = userOptional.get();
            log.info("Пользователь {} найден в базе данных", email);
        } else {
            log.info("Создание нового пользователя: {}", email);

            user = User.builder()
                    .email(email)
                    .name(name)
                    .role(Role.ROLE_USER)
                    .build();

            userRepository.save(user);
            log.info("Создан новый пользователь: {}", email);
        }

        String accessToken = userRequest.getAccessToken().getTokenValue();
        return new DefaultOAuth2User(Collections.singleton(new SimpleGrantedAuthority(user.getRole().getAuthority())),
                oAuth2User.getAttributes(), "email");
    }

    public UserProfileDto buildUserProfile(OAuth2User principal) {

        Object idObj = principal.getAttribute("id");
        Long id = (idObj instanceof Integer) ? Long.valueOf(((Integer) idObj).longValue()) : (Long) idObj;
        String name = principal.getAttribute("name");
        String login = principal.getAttribute("login");
        String email = principal.getAttribute("email");

        return UserProfileDto.builder()
                .name(name)
                .login(login)
                .id(id)
                .email(email)
                .build();
    }
}