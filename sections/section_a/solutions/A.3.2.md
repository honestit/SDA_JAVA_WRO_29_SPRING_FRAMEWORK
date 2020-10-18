Zmodyfikowana klasa `ConsolePrinter` z zewnętrzą zależnością do strumienia, do którego pisze:

```java
package pl.hit.spring.core.components;

import org.springframework.stereotype.Component;

import java.io.PrintStream;

@Component
public class ConsolePrinter implements Printer {

    private PrintStream stream;

    public ConsolePrinter(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void print(String message) {
        stream.println(message);
    }
}
```

---

Fabryka printerów, a więc klasa `Printers`:

```java
package pl.hit.spring.core.warmup;

import pl.hit.spring.core.components.ConsolePrinter;
import pl.hit.spring.core.components.DialogPrinter;
import pl.hit.spring.core.components.FilePrinter;
import pl.hit.spring.core.components.Printer;

public class Printers {

    public static Printer consolePrinter() {
        return new ConsolePrinter(System.out);
    }

    public static Printer errorConsolePrinter() {
        return new ConsolePrinter(System.err);
    }

    public static Printer filePrinter() {
        return new FilePrinter();
    }

    public static Printer dialogPrinter() {
        return new DialogPrinter();
    }
}
```

---

Na koniec klasa `HelloComponent` wykorzystująca fabrykę `Printers`:

```java
package pl.hit.spring.core.warmup;

import pl.hit.spring.core.components.Printer;

public class HelloWorld {

    private Printer printer;

    public HelloWorld(Printer printer) {
        this.printer = printer;
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void sayHello() {
        printer.print("Hello, world");
    }

    public static void main(String[] args) {
        HelloWorld firstHello = new HelloWorld(Printers.consolePrinter());
        firstHello.sayHello();

        HelloWorld secondHello = new HelloWorld(Printers.filePrinter());
        secondHello.sayHello();

        HelloWorld thirdHello = new HelloWorld(Printers.dialogPrinter());
        thirdHello.sayHello();
    }
}
```