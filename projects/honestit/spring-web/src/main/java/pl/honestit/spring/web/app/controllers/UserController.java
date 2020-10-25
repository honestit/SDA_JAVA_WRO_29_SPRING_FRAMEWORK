package pl.honestit.spring.web.app.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.honestit.spring.web.app.model.User;
import pl.honestit.spring.web.app.session.UserIdGenerator;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserIdGenerator userIdGenerator;

    @ModelAttribute("countries")
    public List<String> countries() {
        return Arrays.asList("Polska", "Niemcy", "Francja", "Anglia");
    }

    @GetMapping("/{id:\\d+}") // /users/90
    public String showUser(@PathVariable Long id, Model model) {
        User user = User.builder()
                .id(id)
                .firstName("Jan").lastName("Kowalski")
                .age(70).gender("Mężczyzna").country("Uzbekistan")
                .build();
        model.addAttribute("user", user);
//        model.addAttribute("userFake", user);
//        model.addAttribute(user);
        return "user"; //
    }

    @GetMapping("/add")
    public String prepareUserCreation() {
        return "add-user";
    }

    @PostMapping("/add")
    public String createUser(String firstName, String lastName, Integer age, String gender, String country, Model model) {
        User user = new User();
        Long id = userIdGenerator.getNextId();

        // Uzupełniamy pola użytkownika na podstawie przesłanych parametrów
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setGender(gender);
        user.setCountry(country);

        // Dodajemy użytkownika do modelu i zwracamy ten sam identyfikator widoku,
        // którego używaliśmy do wyświetlenia losowego użytkownika.
        model.addAttribute("user", user);
        // Zwróć uwagę, że nie zwracamy tutaj obiektu User a tekst
        // "user", który odnosi się do strony HTML: /templates/user.html
        return "user";
    }
}
