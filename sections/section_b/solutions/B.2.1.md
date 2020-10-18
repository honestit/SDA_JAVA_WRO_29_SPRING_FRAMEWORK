W pierwszym kontrolerze mamy mapowanie na metodzie, bo pojedyncze mapowanie nie zasługuje na uwagę kontrolera `IntroController`:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class IntroController {

    @GetMapping("/intro")
    public String get() {
        return "IntroController.get";
    }
}
```

---

W drugim przypadku, `FillFormController`, jest podobnie. Zmienia się tylko metoda HTTP, którą wskazujemy do obsługi:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
public class FillFormController {

    @PostMapping("/fill-form")
    public String post() {
        return "FillFormController.post";
    }
}
```

---

Trzeci wariant, to już jedna ścieżka i dwie metody HTTP, więc wygodniej będzie zrobić mapowanie na kontrolerze, a metody HTTP rozbić na metody klasy (`TestRegisterController`):

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/test-register")
public class TestRegisterController {

    @GetMapping
    public String get() {
        return "TestRegisterController.get";
    }

    @PostMapping
    public String post() {
        return "TestRegisterController.post";
    }
}
```

---

Wersja czwarta, to już rozbudowany kontroler obsługujący różne operacje dokonywane na encji `Book` (teoretycznej). Pełni rolę tzw. _CRUD Controller_. Mapowanie kontrolera mamy na główny fragment ścieżki, a metod na konkretne metody HTTP i dalsze fragmenty ścieżki (`TestBookController`):

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/test-book")
public class TestBookController {

    @GetMapping
    public String get() {
        return "TestUserController.get";
    }

    @PostMapping("/add")
    public String add() {
        return "TestUserController.add";
    }

    @PostMapping("/edit")
    public String edit() {
        return "TestUserController.edit";
    }

    @PostMapping("/delete")
    public String delete() {
        return "TestUserController.delete";
    }
}
```

---

Na końcu ostatni wariant, czyli obsługa konkretnego obiektu użytkownika z opcją jego utworzenia, edycji, usunięcia lub podglądu (`TestUserController`). Jest to rozwiązanie w stylu architektury _REST_.

...

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody
@RequestMapping("/test-user")
public class TestUserController {

    @RequestMapping(method = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})
    public String hahaha() {
        return "TestUserController.hahaha";
    }
}
```

... oczywiście to żart, a rozwiązanie powinno wyglądać tak:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@ResponseBody
@RequestMapping("/test-user")
public class TestUserController {

    @GetMapping
    public String get() {
        return "TestUserController.get";
    }

    @PostMapping
    public String post() {
        return "TestUserController.post";
    }

    @PutMapping
    public String put() {
        return "TestUserController.put";
    }

    @DeleteMapping
    public String delete() {
        return "TestUserController.delete";
    }
}
```