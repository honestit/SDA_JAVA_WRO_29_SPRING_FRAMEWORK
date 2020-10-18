Wykorzystujemy alternatywną konfigurację Spring MVC (konfigurację klasy `ServletDispatcher`), która wymaga od nas rozszerzenia klasy `AbstractAnnotationConfigDispatcherServletInitializer` (tak, to chyba najdłuższa nazwa klasy z jaką do tej pory się spotkałeś/aś :) )

```java
package pl.honestit.spring.mvc.config;

import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

public class DispatcherConfiguration extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class[]{RootConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return new Class[]{WebConfig.class};
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/"};
    }
}
```

---

Należy pamiętać, aby wyłączyć wcześniejszą konfigurację w klasie `ApplicationInitializer` usuwając z niej implementację interfejsu i adnotację `@Override`. W ten sposób stanie się klasą, która kod ma fajny, ale nikt się nią nie interesuje.

```java
package pl.honestit.spring.mvc.config;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

public class ApplicationInitializer {

    public void onStartup(ServletContext servletContext) throws ServletException {
        AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
        rootContext.register(RootConfig.class);
        servletContext.addListener(new ContextLoaderListener(rootContext));

        AnnotationConfigWebApplicationContext webContext = new AnnotationConfigWebApplicationContext();
        webContext.register(WebConfig.class);

        ServletRegistration.Dynamic registration =
                servletContext.addServlet("dispatcher", new DispatcherServlet(webContext));
        registration.addMapping("/");
        registration.setLoadOnStartup(1);
    }
}
```