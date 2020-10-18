Kod kontrolera `HelloController`, który na ścieżce `/hello` obsługuje generowanie zawartości odpowiedzi HTTP, a na ścieżce `/helloJsp` zwraca ścieżkę do widoku, który ma być użyty do wygenerowania odpowiedzi:

```java
package pl.honestit.spring.mvc.web.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class HelloControler {

    @GetMapping("/hello")
    @ResponseBody
    public String sayHello() {
        return "Hello, world!";
    }

    @GetMapping("/helloJsp")
    public String sayHelloWithView() {
        return "/WEB-INF/views/hello.jsp";
    }
}
```

---

Treść strony `hello.jsp` nie gra tutaj większe roli, ale zostanie zamieszczona:

```jsp
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Hello, world!</title>
</head>
<body>
<div style="text-align: center">Hello, world!</div>
</body>
</html>
```

Zwróć uwagę, że strona `hello.jsp` musi być w katalogach: `webapp` -> `WEB-INF` -> `views`.