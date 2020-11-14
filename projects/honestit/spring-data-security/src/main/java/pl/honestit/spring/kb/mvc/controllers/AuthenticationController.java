package pl.honestit.spring.kb.mvc.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.LoggedUserDTO;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping
public class AuthenticationController {

    private UserService userService;

    @Autowired
    public AuthenticationController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/login")
    public String prepareLoginPage() {
        return "login";
    }

//    @PostMapping("/login")
//    public void login(String login, String password, HttpSession session, HttpServletResponse response) throws IOException {
//        boolean validCredentials = userService.checkCredentials(login, password);
//        if (!validCredentials) {
//            response.sendError(401, "Błędne dane logowania");
//            return;
//        }
//
//        LoggedUserDTO user = userService.getUser(login, password);
//        session.setAttribute("user", user);
//        response.sendRedirect("/");
//    }
//
//    @GetMapping("/logout")
//    public String logout(HttpSession session) {
//        session.invalidate();
//        return "redirect:/";
//    }
}
