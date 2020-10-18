Nasza klasa kontrolera wyświetlająca losowego użytkownika, ale o `id` wynikającym ze ścieżki będzie wyglądać następująco (kod klasy `UserController`):

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.hit.spring.domain.User;

@Controller
@RequestMapping("/users")
public class UserController {

    @GetMapping("/{id:[1-9][0-9]*}")
    public String showUser(@PathVariable Long id, Model model) {
        User user = new User();
        user.setId(id);
        user.setFirstName("Jan");
        user.setLastName("Kowalski");
        user.setGender("Male");
        user.setAge(37);
        model.addAttribute("user", user);
        return "user";
    }
}
```

Zwróć uwagę, na sposób pobierania identyfikatora `id` z parametru ścieżki oraz na wykorzystanie dodatkowego parametru typu `Model`. Jeżeli chcemy, aby jakiś obiekt był dostępny w widoku, do którego nasz kontroler przekierowuje, to możemy go dodać jako atrybut do obiektu typu `Model`. Warto abyś pamiętał/a, że na stronach JSP możemy również wykorzystywać wszystkie obiekty, które są dostępne w mapach atrybutów żądania, sesji czy kontekstu aplikacji, nie tylko w mapie atrybutów modelu.

---

Kod strony JSP `user.jsp`, odpowiedzialnej za wyświetlenie danych użytkownika, może wyglądać jak niżej:

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" isELIgnored="false" %>
<html>
<head>
    <title>Profil użytkownika</title>
</head>
<body>
    <div>Użytkownik o id: ${user.id}</div>
    <div>Imię: ${user.firstName}</div>
    <div>Nazwisko: ${user.lastName}</div>
    <div>Wiek: ${user.age}</div>
    <div>Płeć: ${user.gender}</div>
</body>
</html>
```

W kontrolerze obiekt użytkownika został dodany pod nazwą atrybutu `user` i tym samym mamy na stronie JSP do dyspozycji obiekt klasy `User` jako zmienną właśnie o tej nazwie.

---

Nasz kontroler `UserController` zostanie rozszerzony o dwie metody główne. Pierwsza ma obsługiwać wyświetlanie formularza (i jest prostym przekierowaniem do widoku), a druga obsługiwać dane z formularza. Poniżej została zaprezentowana ich implementacja.

```java
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
        // "user", który odnosi się do strony JSP: /WEB-INF/views/user.jsp
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
```

Wprowadziliśmy również metodę pomocniczą (`nextIdFromSession`), która obsługuje elementy związane z przechowywaniem kolejnego identyfikatora użytkownika w sesji (jako atrybut). Dzięki temu, że utworzonego w metodzie `createUser` użytkownika wstawiamy do modelu pod tą sąmą nazwą atrybutu, co przy metodzie `showUser` generującej losowe dane, to możemy wykorzystać dokładnie ten sam widok JSP, a więc stronę `user.jsp`, do wyświetlenia danych.

---

Formularz umożliwiający dodanie użytkownika może wyglądać jak niżej:

```jsp
<%@ page isELIgnored="false" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Dodawanie użytkownika</title>
</head>
<body>
    <h1>Dodaj nowego użytkownika</h1>
    <div>
        <form method="post" action="/users/add">
            <fieldset>
                <legend>Dane identyfikacyjne</legend>
                <div><label for="firstName">Imię: </label>
                    <input type="text" name="firstName" id="firstName"/></div>
                <div><label for="lastName">Nazwisko: </label>
                    <input type="text" name="lastName" id="lastName"/></div>
            </fieldset>
            <fieldset>
                <legend>Dane osobowe</legend>
                <div><label for="age">Wiek: </label>
                <input type="number" min="9" max="120" step="1" name="age" id="age"/></div>
                <div><label for="gender">Płeć: </label>
                <select name="gender" id="gender">
                    <option value="-" selected>-- Wybierz płeć --</option>
                    <option value="Mężczyzna">Mężczyzna</option>
                    <option value="Kobieta">Kobieta</option>
                </select>
                </div>
            </fieldset>
            <div>
                <input type="submit" value="Dodaj"/>
                <input type="reset" value="Wyczyść"/>
            </div>
        </form>
    </div>
</body>
</html>
```

Najważniejsze dla nas jest to, aby odpowiednie kontrolki miały nazwy (wartość atrybutu `name`) zgodne z parametrami żądania, których oczekujemy w metodzie kontrolera. Pamiętaj także o atrybutach `method` oraz `action` w tagu `form` i znaczeniu tych atrybutów.

---

Przeprowadzone testy powinny dać Ci następujące obserwacje:
1. Zmieniając nazwę pola formularza `age` na `someNastyAge` sprawiamy, że ze strony będzie przesyłana wartość pod innym kluczem. W metodzie kontrolera oczekujemy zaś parametru o nazwie `age`. Jeżeli taki parametr się nie pojawi, to Spring MVC automatycznie przerwie obsługę żądania błędem `400 Bad Request`. Możemy parametr `age` w metodzie kontrolera oznaczyć adnotacją `RequestParam(required=false)`, ale w tym przypadku nie jest to dobre rozwiązanie, bo oczekujemy tego parametry od użytkownika. Podsumowując: zapamiętaj, że parametry metody muszą odpowiadać jakimś obiektom uniwersalnym, takim jak `Model` czy `HttpSession` lub **być zmapowane z parametrów żądania**. Domyślnie są silnie wymagane, a ich mapowanie (wiązanie) opiera się na nazwie pola formularza i nazwie parametru metody kontrolera.
1. Dodanie nowego pola do formularza nie jest błędem. Jego wartość zostanie przesłana wraz z innymi polami do kontrolera, ale nie zostanie obsłużona (bo kontroler jej nie potrzebuje).
1. Zmiana nazwy atrybutu utworzonego użytkownika z `user` na `newUser` w kodzie kontrolera nie spowoduje żadnego błędu. Natomiast na widoku już tak. Widok `user.jsp` bazuje na zmiennej reprezentującej użytkownika i nazwanej `user`. Taka zmienna musi być dostępna w modelu albo w odpowiedniej mapie związanej z zasięgiem żądania, sesji czy aplikacji. Teraz metoda `showUser` udostępnia tą zmienną poprzez model, ale metoda `createUser` udostępnia już inną zmienną (`newUser`). W efekcie nie możemy ponownie użyć strony `user.jsp`.