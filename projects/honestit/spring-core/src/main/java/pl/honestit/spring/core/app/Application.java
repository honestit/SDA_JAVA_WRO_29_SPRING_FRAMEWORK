package pl.honestit.spring.core.app;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.honestit.spring.core.warmup.HelloWorld;

public class Application {

    public static void main(String[] args) {

    }

    public static void classPathXmlContext() {
        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("beans.xml");

        HelloWorld bean = context.getBean(HelloWorld.class);
        bean.sayHello();
    }

    public static void genericContext() {
        GenericApplicationContext context = new GenericApplicationContext();
        context.registerBean("helloWorld", HelloWorld.class);
        context.refresh();

        HelloWorld bean = (HelloWorld) context.getBean("helloWorld");
        bean.sayHello();

        bean = context.getBean("helloWorld", HelloWorld.class);
        bean.sayHello();

        bean = context.getBean(HelloWorld.class);
        bean.sayHello();
    }
}
