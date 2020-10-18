Konfiguracja obiektu `ViewResolver`, która pozwala nam w kontrolerach zwracać krótsze identyfikatory widoków. Konfiguracja znajduje się w klasie `WebConfig`:

```java
package pl.hit.spring.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

@Configuration
@ComponentScan(basePackages = "pl.honestit.spring.mvc.web")
public class WebConfig implements WebMvcConfigurer {

    @Bean
    public ViewResolver viewResolver() {
        InternalResourceViewResolver resolver = new InternalResourceViewResolver();
        resolver.setPrefix("/WEB-INF/views/");
        resolver.setSuffix(".jsp");
        return resolver;
    }

}
```

---

Zmodyfikowany kod kontrolera `HelloController`, który zwraca identyfiaktor widoku bez uwzględnionego prefiksu i sufiksu:

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
        // TUTAJ
        return "hello";
    }
}
```