Implementacja metody `raw` obsługującej parametry ścieżki z wykorzystaniem obiektu `HttpServletRequest`:

```java
    @GetMapping("/raw/**")
    public String raw(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] segments = requestURI.substring(requestURI.indexOf("/paths/user")).split("/");

        String id = null;
        String operation = null;

        if (segments.length == 5) {
            id = segments[4];
            operation = null;
        } else if (segments.length == 6) {
            id = segments[4];
            operation = segments[5];
        }

        if (id == null && operation == null) {
            return "Brak wartości w ścieżce";
        } else if (operation == null) {
            return "id=" + id;
        } else if (id == null) {
            return "operation=" + operation;
        } else {
            return "id=" + id + " i operation=" + operation;
        }
    }
```

---

Implementacja metody `spring` opartej na adnotacjach `@PathVariable`:

```java
    @GetMapping("/spring/{id}/{operation}")
    public String spring(@PathVariable("id") String id,
                         @PathVariable String operation) {
        return "id=" + id + " i operation=" + operation;
    }
```

---

Implementacja metody `spring` wykorzystującej wyrażenie regularne w celu sprecyzowania formatu zmienną ścieżki `id`:

```java
    @GetMapping("/spring/{id:[1-9][0-9]*}/{operation}")
    public String spring(@PathVariable("id") Long id,
                         @PathVariable String operation) {
        return "id=" + id + " i operation=" + operation;
    }
```

---

Implementacja wykorzystująca wyrażenie regularne w celu sprecyzowania formatu zmiennej ścieżki `operation`:

```java
    @GetMapping("/spring/{id:[1-9][0-9]*}/{operation:get|add|edit|delete}")
    public String spring(@PathVariable("id") Long id,
                         @PathVariable String operation) {
        return "id=" + id + " i operation=" + operation;
    }
```

---

Pełen kod kontrolera `PathParamsController`:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@ResponseBody
@RequestMapping("/paths/user")
public class PathParamsController {

    @GetMapping("/raw/**")
    public String raw(HttpServletRequest request) {
        String requestURI = request.getRequestURI();
        String[] segments = requestURI.substring(requestURI.indexOf("/paths/user")).split("/");

        String id = null;
        String operation = null;

        if (segments.length == 5) {
            id = segments[4];
            operation = null;
        } else if (segments.length == 6) {
            id = segments[4];
            operation = segments[5];
        }

        if (id == null && operation == null) {
            return "Brak wartości w ścieżce";
        } else if (operation == null) {
            return "id=" + id;
        } else if (id == null) {
            return "operation=" + operation;
        } else {
            return "id=" + id + " i operation=" + operation;
        }
    }

    @GetMapping("/spring/{id:[1-9][0-9]*}/{operation}")
    public String spring(@PathVariable("id") Long id,
                         @PathVariable String operation) {
        return "id=" + id + " i operation=" + operation;
    }
}
```