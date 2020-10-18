
**Kod interfejsu `Printer`:**

```java
package pl.honestit.spring.core.components.printers;

public interface Printer {

    void print(String message);
}
```

---

**Kod implementacji `ConsolePrinter`:**

```java
package pl.honestit.spring.core.components.printers;

import org.springframework.stereotype.Component;

@Component
public class ConsolePrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
```

---

**Kod implementacji `DialogPrinter`:**

```java
package pl.honestit.spring.core.components.printers;

import org.springframework.stereotype.Component;

import javax.swing.*;

@Component
public class DialogPrinter implements Printer {

    @Overrideoid print(String message) {
        JOptionPane.showMessageDialog(null, message);
    }
}

```

---

**Kod implementacji `FilePrinter`:**

```java
package pl.honestit.spring.core.components.printers;

import org.springframework.stereotype.Component;

import java.io.*;

@Component
public class FilePrinter implements Printer {

    @Override
    public void print(String message) {
        String userHomePath = System.getProperty("user.home");
        Path outputPath = Paths.get(userHomePath, "out.log");
        try (PrintWriter printWriter = new PrintWriter(new FileWriter(outputPath.toFile(), true))) {

            printWriter.println(message);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

**Testy komponentów w metodzie `main` klasy `PrinterApplication`:**

```java
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.register(WarmUpConfiguration.class);
        context.scan(new String[]{"pl.honestit.spring.core.warmup",
                "pl.honestit.spring.core.components"});
        context.refresh();

        ConsolePrinter consolePrinter = context.getBean("consolePrinter", ConsolePrinter.class);
        consolePrinter.print("Hello, world! 1.");

        ConsolePrinter sameConsolePrinter = context.getBean(ConsolePrinter.class);
        sameConsolePrinter.print("Hello, world! 2.");

        DialogPrinter dialogPrinter = context.getBean(DialogPrinter.class);
        dialogPrinter.print("Hello, world!");

        FilePrinter filePrinter = context.getBean(FilePrinter.class);
        filePrinter.print("Hello, world");

        /*
           To wywołanie nie zadziała, ponieważ istnieją trzej kandydaci
           którzy aspirują do bycia beanem typu Printer, a nazywają się:

           - consolePrinter
           - dialogPrinter
           - filePrinter
        */

        Printer printer = context.getBean(Printer.class);

        /*
           To zadziała, bo chociaż chcemy interfejs Printer, to
           wskazujemy na konkretnego bean'a po jego nazwie
        */
        Printer printer = context.getBean("dialogPrinter", Printer.class);
        printer.print("Wow! Wow! Wow! Loose Coupling!!");
    }
```

