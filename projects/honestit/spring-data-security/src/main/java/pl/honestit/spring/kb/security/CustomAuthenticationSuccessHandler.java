package pl.honestit.spring.kb.security;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.LoggedUserDTO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Component @Slf4j @RequiredArgsConstructor
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.debug("Udane logowanie: {}", authentication);

        // Pobieramy użytkownika z serwisu na podstawie informacji o zalogowanym użytkowniku
        String username = authentication.getName();
        LoggedUserDTO user = userService.getUser(username);
        log.debug("Użytkownik z db: {}", user);

        // Zapisujemy użytkownika w sesji, aby był dostępny w zgodzie z dotychczasową
        // implementacją kontrolerów
        HttpSession session = request.getSession();
        session.setAttribute("user", user);

        // Robimy przekierowanie
        new DefaultRedirectStrategy().sendRedirect(request, response, "/");
    }
}
