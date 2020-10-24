Kod kontrolera `HelloController`, który na ścieżce `/hello` obsługuje generowanie zawartości odpowiedzi HTTP, 

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
}
```