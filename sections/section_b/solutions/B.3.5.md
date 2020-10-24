Zaczynamy od rozszerzenia klasy `User` o dodatkowe pola:

```java
package pl.honestit.spring.mvc.domain;

import java.time.LocalDateTime;
import java.util.Objects;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String country;
    private LocalDateTime creationDate;
    private LocalDateTime lastModificationDate;
    private Long versionId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public LocalDateTime getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    public LocalDateTime getLastModificationDate() {
        return lastModificationDate;
    }

    public void setLastModificationDate(LocalDateTime lastModificationDate) {
        this.lastModificationDate = lastModificationDate;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", creationDate=" + creationDate +
                ", lastModificationDate=" + lastModificationDate +
                ", versionId=" + versionId +
                '}';
    }
}
```
--- 

Stwórzmy klasę `UserDTO`, która będzie implementowała wzorzec Data Transfer Object dla naszego użytkownika. Zgodnie z poleceniem, pozbawimy ją technicznych aspektów, a więc pól związanych z datą utworzenia czy ostatnią datą modyfikacji. Obiekty `UserDTO` będziemy tworzyć na podstawie istniejących w pamięci obiektów klasy `User`, więc dorzucimy sobie również konstruktor, który pozwoli nam szybko stworzyć obiekt bez potrzeby wywoływania wielu setterów. Nie zapomnij tylko, aby w klasie był dostępny także konstrukt bezparametrowy.

```java
package pl.honestit.spring.mvc.dto;

import java.time.LocalDateTime;
import java.util.Objects;

public class UserDTO {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String country;
    private Long versionId;

    public UserDTO() {
    }

    public UserDTO(Long id, String firstName, String lastName, Integer age, String gender, String country, Long versionId) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.country = country;
        this.versionId = versionId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Long getVersionId() {
        return versionId;
    }

    public void setVersionId(Long versionId) {
        this.versionId = versionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDTO userDTO = (UserDTO) o;
        return Objects.equals(id, userDTO.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "UserDTO{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", age=" + age +
                ", gender='" + gender + '\'' +
                ", country='" + country + '\'' +
                ", versionId=" + versionId +
                '}';
    }
}
```

---

Przyszedł czas na implementację pierwszego serwisu, a więc klasy `UserService`. Jako sposób przechowywania użytkowników wybieramy mapę. Pod kluczem będziemy mieli `id` użytkownika, a wartością będzie sam obiekt użytkownika. Warto pamiętać, że obiekt klasy `UserService` będzie komponentem, a te domyślnie funkcjonują jako Singletony. Oznacza to, że wiele żądań może się do tego komponentu odwowyłać równocześnie. Musimy zatem korzystać z implementacji mapy bezpiecznej w kontekście wielowątkowym.

```java
package pl.honestit.spring.mvc.core.services;

import org.springframework.stereotype.Service;
import pl.honestit.spring.mvc.domain.User;
import pl.honestit.spring.mvc.dto.UserDTO;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Service
public class UserService {

    private AtomicLong nextUserId = new AtomicLong(1L);
    private ConcurrentHashMap<Long, User> users = new ConcurrentHashMap<>();
}

```

Uzupełniamy nasz serwis o metody do zapisu użytkownika oraz do pobrania użytkownika. Zwróć uwage, że zapis użytkownika odbywa się na podstawie obiektu `UserDTO`, ale tworzymy obiekt `User` i taki obiekt przechowujemy w naszej _mapo-pamięci_. Podobnie przy pobraniu użytkownika - odnajdujemy obiekt klasy `User`, ale na zewnątrz udostępniamy go w postaci obiektu klasy `UserDTO`.

```java
    public Long saveUser(UserDTO userDTO) {
        User user = new User();
        // Korzystamy z aktualnej wartości obiektu nextUserId i zwiększamy
        // ją o 1
        user.setId(nextUserId.getAndIncrement());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAge(userDTO.getAge());
        user.setGender(userDTO.getGender());
        user.setCountry(userDTO.getCountry());
        // Nowy użytkownik zawsze zaczyna z pierwszą wersją
        user.setVersionId(1L);
        user.setCreationDate(LocalDateTime.now());
        // Datę ostatniej modyfikacji ustawiamy jako datę utworzenia, aby na początku
        // te daty były ze sobą zgodne.
        user.setLastModificationDate(user.getCreationDate());

        users.put(user.getId(), user);
        // Zwracamy id pod jakim został zapisany użytkownik
        return user.getId();
    }

    public UserDTO getUser(Long id) {
        User user = users.get(id);
        if (user == null) {
            return null;
        }
        UserDTO userDTO = new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getAge(),
                user.getGender(),
                user.getCountry(),
                user.getVersionId()
        );
        return userDTO;
    }
```

---

Ostatni element to wykorzystanie klasy `UserDTO` oraz `UserService` w kontrolerze `UserController`. Musimy zmodyfikować metodę zwracającą użytkownika dla podanego `id` oraz metodę zapisującą użytkownika. Również konieczne jest wstrzyknięcie naszego serwisu do kontrolera jako bean'a.

Wstrzyknięcie:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.honestit.spring.mvc.core.services.UserService;
import pl.honestit.spring.mvc.domain.User;
import pl.honestit.spring.mvc.dto.UserDTO;

import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    // Przy pojedynczym konstruktorze nie musimy używać adnotacji @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    // ... reszta kodu
}

```

Pobieranie użytkownika:

```java
    @GetMapping("/{id:[1-9][0-9]*}")
    public String showUser(@PathVariable Long id, Model model) {
        // Pobieramy użytkownika z serwisu
        UserDTO user = userService.getUser(id);
        model.addAttribute("user", user);
        return "user";
    }
```

Zapis użytkownika (zwróć uwagę, że nie korzystamy już z pobierania `id` z sesji, bo teraz nadawaniem `id` zajmuje się serwis):

```java
    @PostMapping("/add")
    public String createUser(UserDTO user, Model model) {
        // Spring automatycznie stworzy nam obiekt user i wypełni jego pola dostępnymi wartościami
        
        // Zapisujemy użytkownika w serwisie
        Long id = userService.saveUser(user);
        user.setId(id);

        // Dodajemy użytkownika do modelu i zwracamy ten sam identyfikator widoku,
        // którego używaliśmy do wyświetlenia losowego użytkownika.
        model.addAttribute("user", user);
        return "user";
    }
```

Może być dla Ciebie czymś zastanawiającym, że w żaden sposób nie musieliśmy modyfikować widoków. Wynika to z tego, że nasza klasa `UserDTO` i `User` posiadają wspólne pola, które się nie zmieniły. Stąd przejście na klasę `UserDTO` było niewidoczne z perspektywy kodu widoku. Zobacz, że obiekty typu `model` pozwala zapisywać atrybuty pod kluczem typu `String`, a wartość przyjmuje typu `Object`. Stąd i tak na poziomie pobierania zapisanego atrybutu na stronie html ważne jest tylko, aby atrybuty miały ten sam zestaw metod typu `get`. W ten sposób zmienne na stronach html zachowują się jakby nie miały typu. Dopiero w trakcie wykonania okaże się czy jest w nich dostępne pole (metoda typu `get`), którego oczekujemy czy nie.

W metodzie `createUser` skorzystaliśmy też z bajkowego wsparcia Spring MVC do tworzenia obiektów. Nie podajemy już listy parametrów, a pojedynczy parametry typu `UserDTO`. Zmieniają się w tym wszystkim nieco zasady gry, bo teraz nie mamy co liczyć na otrzymanie błędu `400 Bad Request` przy niepoprawnych nazwach pól w formularzu. Parametr `user` zostanie zawsze stworzony (na podstawie konstruktora bezparametrowego) i nigdy nie będzie w kodzie metody `null'em`. Przesłane parametry żądania posłużą do wypełnienia pól naszego obiektu typu `UserDTO` (pola te muszą posiadać `settery`). Błąd `400 Bad Request` możemy teraz spotkać tylko w sytuacji, gdy z formularza wychodzi pole pod nazwą zgodną z nazwą pola w klasie `UserDTO`, ale wartość tego pola jest niekonwertowalna do pola obiektu. Przykładowo w polu formularza dla wieku wpisalibyśmy coś, co nie jest liczbą. 

Zwróć uwagę, że wykorzystanie jako parametru metody całego obiektu `UserDTO` sprawia, że dodawanie nowych pól do klasy `UserDTO` i do formularza nie wymaga od nas zmiany kodu kontrolera. To bardzo dobra wiadomość!

Kolejna rzecz, którą trzeba jeszcze zauważyć, to to, że nasz kontroler nie wykonuje teraz żadnego przetwarzania danych. Zajmuje się ich odebraniem, przekazaniem do serwisu oraz udostępnieniem wyników przetwarzania do widoku. Samo przetwarzanie danych przeszło do serwisu. Z perspektywy kod w serwisie nie ma natomiast znaczenia gdzie został stworzony obiekt klasy `UserDTO`. Obie klasy chociaż mają ze sobą relację, to działają od siebie niezależnie i mają swoje indywidualne odpowiedzialności.