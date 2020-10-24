Uzupełniony kod klasy `SecurityConfig`. Puste implementacje metod `configure` sprawiają, że w naszej aplikacji nie działa żadna warstwa Security (poza naszą wcześniejszą opartą na filtrze).

```java
package pl.honestit.spring.kb.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ;
    }
}
```

Należy również uzupełnić klasę `RootConfig` o adnotację `@EnableWebSecurity`. Klasa ta ostatecznie będzie wyglądać następująco:

```java
package pl.honestit.spring.kb;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;


@Configuration
@ComponentScan
@EnableWebMvc
@EnableJpaRepositories(basePackages = "pl.honestit.spring.kb.data.repository")
@EnableTransactionManagement
@EnableWebSecurity
public class RootConfig {

}
```

Na koniec zostaje nam dorzucenie klasy `SecurityInitializer` o kodzie jak niżej:

```java
package pl.honestit.spring.kb.config;

import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

public class SecurityInitializer extends AbstractSecurityWebApplicationInitializer {
}
```