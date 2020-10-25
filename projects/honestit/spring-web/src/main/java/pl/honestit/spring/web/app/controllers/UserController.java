package pl.honestit.spring.web.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import pl.honestit.spring.web.app.dto.UserDTO;
import pl.honestit.spring.web.app.services.UserService;

import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @ModelAttribute("countries")
    public List<String> countries() {
        return List.of("Polska", "Niemcy", "Francja", "Anglia");
    }

    @GetMapping("/{id:\\d+}")
    public String showUser(@PathVariable Long id, Model model) {
        UserDTO user = userService.getUser(id);
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Użytkownik o id = " + id + "nie istnieje");
        }
        model.addAttribute("user", user);
        return "user";
    }

    @GetMapping("/add")
    public String prepareUserCreation() {
        return "add-user";
    }

    @PostMapping("/add")
    public String createUser(UserDTO userDTO) {
        Long userId = userService.saveUser(userDTO);
        return "redirect:/users/" + userId;
    }
}
