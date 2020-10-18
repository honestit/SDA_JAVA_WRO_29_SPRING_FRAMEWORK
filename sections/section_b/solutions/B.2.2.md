Metoda do obsługi parametrów z wykorzystaniem obiektu `HttpServletRequest`. Zwróć uwagę na potrzebę obsługi `null` kiedy któryś z parametrów nie został podany.

```java
    @GetMapping("/raw")
    public String serveRawParameters(HttpServletRequest request) {
        String first = request.getParameter("first");
        String second = request.getParameter("second");

        if (first == null && second == null) {
            return "Brak parametrów";
        }
        else if (first == null) {
            return "second=" + second;
        }
        else if (second == null) {
            return "first=" + first;
        }
        else {
            return "first=" + first + " i second=" + second;
        }
    }
```

---

Metoda do obsługi parametrów z wykorzystaniem adnotacji `@RequestParam`. Ważne aby zwrócić uwagę, że w tej wersji nie ma sensu sprawdzać czy parametry metody są `null`'ami, bo jeżeli by były, to mechanizm Spring'a w ogóle nie wywoła metody naszego kontrolera, a zwróci do klienta błąd `400 Bad Request`.

```java
    @GetMapping("/spring")
    public String serveSpringParameters(@RequestParam("first") String first,
                                        @RequestParam String second) {

        return "first=" + first + " i second=" + second;
    }
```

--- 

Pełen kod kontrolera `ParametersController`:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/parameters")
public class ParametersController {

    @GetMapping("/raw")
    public String serveRawParameters(HttpServletRequest request) {
        String first = request.getParameter("first");
        String second = request.getParameter("second");

        if (first == null && second == null) {
            return "Brak parametrów";
        }
        else if (first == null) {
            return "Second=" + second;
        }
        else if (second == null) {
            return "First=" + first;
        }
        else {
            return "First=" + first + " i Second=" + second;
        }
    }

    @GetMapping("/spring")
    public String serveSpringParameters(@RequestParam("first") String first,
                                        @RequestParam String second) {

        return "First=" + first + " i Second=" + second;
    }
}
```

---

Uzupełnienie adnotacji `@RequestParam` o brak wymagalności i wartość domyślną w metodzie `serveSpringParameters`. W takiej wersji powinniśmy już ręcznie sprawdzić czy parametr metody `first` jest `null'em`, bo teraz już może być. Parametr `second`, gdy nie zostanie przekazany w żądaniu przez klienta, czyli będzie `null'em`, to zostanie przypisana mu wartość domyślna `Abrakadabra`. Stąd efektywnie w kodzie dalej nie będzie miał wartości `null`. Spring MVC (ale również inne moduły/projekty Spring) starają się wyeliminować problem `NullPointerException`.

```java
    @GetMapping("/spring")
    public String serveSpringParameters(@RequestParam(value = "first", required = false) String first,
                                        @RequestParam(defaultValue = "Abrakadabra") String second) {

        if (first == null) {
            return "second=" + second;
        }
        else {
            return "first=" + first + " i second=" + second;
        }
    }
```