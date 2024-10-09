package ru.javacode.springmvclibrary.security;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler {

    private final TokenRevocationService tokenRevocationService;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException {
        if (authentication != null && authentication.getPrincipal() instanceof CustomOAuth2User principal) {
            String accessToken = principal.getAccessToken();
            if (accessToken != null) {
                boolean revoked = tokenRevocationService.revokeToken(accessToken);
                if (revoked) {
                    log.info("OAuth2 токен успешно отозван.");
                } else {
                    log.warn("Не удалось отозвать OAuth2 токен.");
                }
            }
        }
        response.sendRedirect("/"); // Перенаправление на главную страницу после выхода
    }
}
