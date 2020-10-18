Kod klasy `HelloWorld`:

```java
package pl.honestit.spring.core.warmup;

public class HelloWorld {

    public void sayHello() {
        System.out.println("Hello, world!");
    }
}

```


Kod klasy `Application`, w którym tworzymy kontekst, rejestrujemy beana i go pobieramy, aby użyć:

```java
package pl.honestit.spring.core.app;

import org.springframework.context.support.GenericApplicationContext;
import pl.honestit.spring.core.warmup.HelloWorld;

public class Application {

    public static void main(String[] args) {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("helloBean", HelloWorld.class);
        context.refresh();

        HelloWorld bean = context.getBean("helloBean", HelloWorld.class);
        bean.sayHello();
    }
}

```