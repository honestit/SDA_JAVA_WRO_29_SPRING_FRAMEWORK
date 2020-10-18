**Klasa `HelloWorld` z pojedynczą zależnością i wstrzyknięciem na poziomie konstruktora:**

```java
package pl.honestit.spring.core.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.honestit.spring.core.components.printers.ConsolePrinter;

@Component
public class HelloWorld {

    private ConsolePrinter consolePrinter;

    @Autowired
    public HelloWorld(ConsolePrinter consolePrinter) {
        this.consolePrinter = consolePrinter;
    }

    public void sayHello() {
        consolePrinter.print("Hello, world!");
    }
}
```

---

**Klasa `HelloWorld` z podwójną zależnością i wstrzyknięciem na poziomie metody:**

```java
package pl.honestit.spring.core.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.honestit.spring.core.components.printers.ConsolePrinter;
import pl.honestit.spring.core.components.printers.FilePrinter;

import java.io.File;

@Component
public class HelloWorld {

    private ConsolePrinter consolePrinter;
    
    private FilePrinter filePrinter;

    @Autowired
    public HelloWorld(ConsolePrinter consolePrinter) {
        this.consolePrinter = consolePrinter;
    }
    
    @Autowired
    public void setFilePrinter(FilePrinter filePrinter) {
        this.filePrinter = filePrinter;
    }

    public void sayHello() {
        consolePrinter.print("Hello, world!");
    }
}
```

---

**Testy komponenty `HelloWorld` w metodzie `main` klasy `PrinterApplication`:**

```java
    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        context.scan("pl.honestit.spring.core.warmup", "pl.honestit.spring.core.components");
        context.refresh();

        HelloWorld helloWorld = context.getBean(HelloWorld.class);
        helloWorld.sayHello();
    }
```

---

**Klasa `HelloWorld` z niedziałającą zaleźnością do interfejsu `Printer`:**

```java
package pl.honestit.spring.core.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.honestit.spring.core.components.printers.ConsolePrinter;
import pl.honestit.spring.core.components.printers.FilePrinter;
import pl.honestit.spring.core.components.printers.Printer;

import java.util.Random;

@Component
public class HelloWorld {

    private ConsolePrinter consolePrinter;

    private FilePrinter filePrinter;

    private Printer somePrinter;

    @Autowired
    public HelloWorld(ConsolePrinter consolePrinter) {
        this.consolePrinter = consolePrinter;
    }

    @Autowired
    public void setFilePrinter(FilePrinter filePrinter) {
        this.filePrinter = filePrinter;
    }

    @Autowired
    public void setSomePrinter(Printer printer) {
        this.somePrinter = printer;
    }

    public void sayHello() {
        if (new Random().nextInt(2) > 0) {
            consolePrinter.print("Hello, world!");
        }
        else {
            filePrinter.print("Hello, world!");
        }
    }
}
```