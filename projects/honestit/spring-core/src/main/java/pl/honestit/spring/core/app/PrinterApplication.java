package pl.honestit.spring.core.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import pl.honestit.spring.core.config.WarmUpConfiguration;
import pl.honestit.spring.core.warmup.HelloWorld;

public class PrinterApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WarmUpConfiguration.class);
        HelloWorld helloWorld = context.getBean("helloWorld", HelloWorld.class);
        helloWorld.sayHello();

        helloWorld.printPrinters();
    }
}
