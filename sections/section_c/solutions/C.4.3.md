Na początek dodajemy do klasy `SecurityConfig` podany kod dostarczający źródło danych:

```java
    @Bean
    private DataSource dataSource() {
        DriverManagerDataSource dataSource = new DriverManagerDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://localhost:3306/knowledge_db?serverTimezone=UTC");
        dataSource.setUsername("root");
        return dataSource;
    }
```

W drugim kroku musimy wykorzystać konfigurację użytkowników opartą o JDBC. Korzystamy w tym celu z metody `jdbcAuthentication`. Dostarczamy w niej informację o źródle danych oraz dwa zapytania, które definiują pobieranie użytkownika oraz pobieranie ról użytkownika. Nasze zapytania będą odwoływać się do tabel `users` oraz `users_roles`. Nasza tabela `users` nie posiada informacji o tym czy użytkownik jest aktywny czy nie. W aplikacji domyślnie użytkownik zawsze jest aktywny. Nie zmienia to jednak faktu, że Spring Security oczekuje od nas tej informacji. Stąd zapytanie zwraca stałą wartość `true` zamiast wartości z kolumny.

Pełna treść konfiguracji autentykacji:

```java
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Konfiguracja oparta na użytkownikach w bazie danych
        auth.jdbcAuthentication()
                .dataSource(dataSource())
                .usersByUsernameQuery("SELECT login, password, true FROM users WHERE login = ?")
                .authoritiesByUsernameQuery("SELECT login, role FROM users_roles WHERE login = ?");
    }
```

---

Powyższa konfiguracja pozwoli nam poprawnie logować się na użytkowników w bazie danych. Potrzebujemy więc już tylko zintegrować użytkownika widzianego oczamu Spring Security z użytkownikiem widzianym naszymi oczami. Wykorzystujemy mechanizm interceptorów, które pozwalają nam się wpiąć z _kodem_ przed wykonanie metody kontrolera bądź po wykonaniu metody kontrolera. Nas interesuje zdarzenie przed, aby móc zapewnić użytkownika w sesji. Interceptory odgrywają tutaj rolę filtrów, ale wobec kontrolerów.

Kod interceptora `SessionInterceptor`:

```java
package pl.honestit.spring.kb.mvc.interceptors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import pl.honestit.spring.kb.core.services.UserService;
import pl.honestit.spring.kb.dto.LoggedUserDTO;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;

public class SessionInterceptor extends HandlerInterceptorAdapter {

    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Principal userPrincipal = request.getUserPrincipal();
        if (userPrincipal != null) {
            if (request.getSession().getAttribute("user") == null) {
                String login = userPrincipal.getName();
                LoggedUserDTO user = userService.getUser(login);
                if (user != null) {
                    request.getSession().setAttribute("user", user);
                }
            }
        }
        return true;
    }
}
```

oraz brakująca implementacja metody `getUser` w `UserService`:

```java
    public LoggedUserDTO getUser(String login) {
        LoggedUserDTO loggedUserDTO = null;
        User user = userRepository.getUserByLogin(login);

        if (user != null) {
            loggedUserDTO = new LoggedUserDTO();
            loggedUserDTO.setId(user.getId());
            loggedUserDTO.setLogin(user.getLogin());
            loggedUserDTO.setFirstName(user.getFirstName());
            loggedUserDTO.setLastName(user.getLastName());
        }
        return loggedUserDTO;
    }
```


Na koniec pozostała nam rejestracja interceptora jako bean'a oraz dodanie go do rejestru interceptorów. Obie operacje wykonujemy w klasie `WebConfig`:

```java

    @Bean
    public SessionInterceptor sessionInterceptor() {
        return new SessionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(sessionInterceptor());
    }
```