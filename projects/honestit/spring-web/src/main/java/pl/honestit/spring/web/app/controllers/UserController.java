package pl.honestit.spring.web.app.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.honestit.spring.web.app.model.User;

import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id:\\d+}") // /users/90
    public String showUser(@PathVariable Long id, Model model) {
        User user = User.builder()
                .id(id)
                .firstName("Jan").lastName("Kowalski")
                .age(70).gender("Mężczyzna")
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
    public String createUser(String firstName, String lastName, Integer age, String gender, Model model, HttpSession session) {
        User user = new User();
        Long id = nextIdFromSession(session);

        // Uzupełniamy pola użytkownika na podstawie przesłanych parametrów
        user.setId(id);
        user.setFirstName(firstName);
        user.setLastName(lastName);
        user.setAge(age);
        user.setGender(gender);

        // Dodajemy użytkownika do modelu i zwracamy ten sam identyfikator widoku,
        // którego używaliśmy do wyświetlenia losowego użytkownika.
        model.addAttribute("user", user);
        // Zwróć uwagę, że nie zwracamy tutaj obiektu User a tekst
        // "user", który odnosi się do strony HTML: /templates/user.html
        return "user";
    }

    private Long nextIdFromSession(HttpSession session) {
        // Sprawdzamy czy w sesji jest nasz atrybutu,
        // a jeżeli nie to ustawiamy wartość początkową
        if (session.getAttribute("nextUserId") == null) {
            session.setAttribute("nextUserId", 1L);
        }

        // Pobieramy bieżącą wartość atrybutu z sesji i nadpisujemy
        // ją wartością o 1 większą. Bieżącą wartość zwracamy
        Long nextUserId = (Long) session.getAttribute("nextUserId");
        session.setAttribute("nextUserId", nextUserId + 1L);
        return nextUserId;
    }

}
