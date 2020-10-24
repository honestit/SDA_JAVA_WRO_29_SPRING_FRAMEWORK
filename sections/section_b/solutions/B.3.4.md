W pierwszej kolejności uzupełniamy klasę `User` o pole `country`. Jest to zwykłe pole tekstowe:

```java
package pl.honestit.spring.mvc.domain;

import java.util.Objects;

public class User {

    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String gender;
    private String country;

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
                '}';
    }
}
```

Następnie w kontrolerze `UserController` dodajemy zaproponowaną metodę `public List<String> countries`, która będzie zwracać gotową listę państw do wykorzystania na stronie w formularzu. Kluczowe jest tutaj aby wykorzystać adnotację `@ModelAttribute`. Taka adnotacja umieszczona na metodzie sprawia, że wartość zwracana z tej metody będzie automatycznie dostępna na wszystkich widokach związanych z tym kontrolerem. Używamy również parametru `name` tej adnotacji, aby określić pod jakim kluczem wynik metody ma być dodany do modelu. Nie zapomnij też, że ta metoda musi być publiczna.

```java
    @ModelAttribute(name = "countries")
    public List<String> countries() {
        return Arrays.asList("Polska", "Niemcy", "Francja", "Anglia");
    }
```

Nie jest to specjalnie skomplikowany kod, a sposób użycia słownika państw możemy zobaczyć na zmodyfikowanym kodzie formularza:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Dodaj nowego użytkownika</title>
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
                <div>
                    <label for="country">Państwo: </label>
                    <select name="country" id="country">
                            <option th:each="country : ${countries}" th:value="${country}" th:text="${country}">Miasto</option>
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

---

Pamiętajmy również o uzupełnienie kodu widoku `user.html` o wyświetlenie państwa użytkownika oraz kodu metody tworzącej użytkownika (`createUser` w `UserController`) o obsługę parametru `country` i zapisanie państwa w obiekcie użytkownika.

Widok użytkownika:

```html
<!DOCTYPE html>
<html lang="en" xmlns:th="http://www.thymeleaf.org">
<head>
    <meta charset="UTF-8">
    <title>Profil użytkownika</title>
</head>
<body>
<div th:text="|Użytkownik o id: ${user.id}|">Id: 0</div>
<div th:text="|Imię: ${user.firstName}|">Imię: Jan</div>
<div th:text="|Nazwisko: ${user.lastName}|">Nazwisko: Kowalski</div>
<div th:text="|Wiek: ${user.age}|">Wiek: 44</div>
<div th:text="|Płeć: ${user.gender}|">Płeć: mężczyzna</div>
<div th:text="|Państwo: ${user.country}|">Państwo: Polska</div>
</body>
</html>
```

Zapis użytkownika:

```java
    @PostMapping("/add")
    public String createUser(String firstName, String lastName, Integer age, String gender, String country, Model model, HttpSession session) {
        User user = new User();
        Long id = nextIdFromSession(session);

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
        return "user";
    }
```