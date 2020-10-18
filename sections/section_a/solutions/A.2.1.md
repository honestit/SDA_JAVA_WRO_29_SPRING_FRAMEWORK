Kod klasy `Application` wykorzystujący `beans.xml`:

```java
package pl.honestit.spring.core.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.honestit.spring.core.warmup.HelloWorld;

public class Application {

    public static void main(String[] args) {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");
        HelloWorld helloBean = context.getBean("helloBean", HelloWorld.class);
        helloBean.sayHello();

    }

    private static void genericWarmUp() {
        
        // Wcześniejszy kod z metody main
    }
}
```


Konfiguracja pliku `beans.xml` w katalogou `resources`:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
       xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans.xsd">

    <bean name="helloBean" class="pl.honestit.spring.core.warmup.HelloWorld"/>

</beans>
```