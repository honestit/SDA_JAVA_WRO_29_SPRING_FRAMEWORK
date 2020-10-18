
Klasa konfiguracyjna `WarmUpConfiguration`:

```java
package pl.honestit.spring.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.honestit.spring.core.warmup.HelloWorld;

@Configuration
public class WarmUpConfiguration {

    @Bean
    public HelloWorld helloBean() {
        return new HelloWorld();
    }
}

```

Wykorzystanie konfiguracji w metodzie main `Application`:

```java
package pl.honestit.spring.core.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.honestit.spring.core.config.WarmUpConfiguration;
import pl.honestit.spring.core.warmup.HelloWorld;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(WarmUpConfiguration.class);
        context.refresh();

        HelloWorld helloBean = context.getBean("helloBean", HelloWorld.class);
        helloBean.sayHello();
    }

    private static void classPathWarmUp() {

        // ...
    }

    private static void genericWarmUp() {
        
        // ...
    }
}

```

Automatyczne wykrywanie komponentu z adnotacją `@Component` w klasie `HelloWorld`:

```java
package pl.honestit.spring.core.warmup;

import org.springframework.stereotype.Component;

@Component
public class HelloWorld {

    public void sayHello() {
        System.out.println("Hello, world!");
    }
}

```

Uzupełnienie metody `main` w klasie `Application` o obsługę automatycznego skanowania:

```java
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(WarmUpConfiguration.class);
        context.scan("pl.honestit.spring.core.warmup");
        context.refresh();

        HelloWorld helloBean = context.getBean("helloBean", HelloWorld.class);
        helloBean.sayHello();
    }
```


Kod testujący różne sposoby pobierania bean'ów:

```java
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(WarmUpConfiguration.class);
        context.scan("pl.honestit.spring.core.warmup");
        context.refresh();

        HelloWorld helloBean = context.getBean("helloBean", HelloWorld.class);
        helloBean.sayHello();

        HelloWorld helloWorld = context.getBean("helloWorld", HelloWorld.class);
        helloWorld.sayHello();

        System.out.println(helloBean == helloWorld);

        HelloWorld helloBean2 = context.getBean("helloBean", HelloWorld.class);
        HelloWorld helloWorld2 = context.getBean("helloWorld", HelloWorld.class);

        System.out.println(helloBean == helloBean2);
        System.out.println(helloWorld == helloWorld2);

        // To nie zadziała - nie istnieje taki bean
        HelloWorld buzzBuzz = context.getBean("buzzBuzz", HelloWorld.class);
        buzzBuzz.sayHello();

        // To też nie zadziała - niejednoznaczna zależność
        HelloWorld bean = context.getBean(HelloWorld.class);
        bean.sayHello();
    }
```