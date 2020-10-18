package pl.honestit.spring.core.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.GenericApplicationContext;
import pl.honestit.spring.core.components.ConsolePrinter;
import pl.honestit.spring.core.components.DialogPrinter;
import pl.honestit.spring.core.components.FilePrinter;
import pl.honestit.spring.core.components.Printer;
import pl.honestit.spring.core.config.WarmUpConfiguration;
import pl.honestit.spring.core.warmup.HelloWorld;

import java.util.Arrays;

public class Application {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(WarmUpConfiguration.class);

        HelloWorld helloBean = context.getBean("helloBean", HelloWorld.class);
        helloBean.sayHello();

        HelloWorld buzzBuzz = context.getBean("buzzBuzz", HelloWorld.class);
        buzzBuzz.sayHello();

        HelloWorld helloWorld = context.getBean("helloWorld", HelloWorld.class);
        helloWorld.sayHello();

        HelloWorld helloWorld2 = context.getBean("helloWorld", HelloWorld.class);

        System.out.println("helloBean == helloWorld: " + (helloBean == helloWorld));
        System.out.println("helloWorld == helloWorld2: " + (helloWorld == helloWorld2));

        

        try {
            HelloWorld whichOne = context.getBean(HelloWorld.class);
            whichOne.sayHello();
        } catch (RuntimeException re) {
            System.err.println(re.getLocalizedMessage());
        }

        ConsolePrinter consolePrinter = context.getBean("consolePrinter", ConsolePrinter.class);
        consolePrinter.print("Jestem konsolą");

        FilePrinter filePrinter = context.getBean("filePrinter", FilePrinter.class);
        filePrinter.print("Sobie do pliku");

        DialogPrinter dialogPrinter = context.getBean("dialogPrinter", DialogPrinter.class);
        dialogPrinter.print("W okienku");

        Printer somePrinter = context.getBean("dialogPrinter", Printer.class);
        somePrinter.print("Gdzie?");

        try {
            Printer bean = context.getBean(Printer.class);
        } catch (RuntimeException re) {
            System.err.println(re.getLocalizedMessage());
        }

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
