Kod klasy `HelloWorld` z polem typu `printer` i luźnymi zależnościami podawanymi z zewnątrz:

```java
package pl.hit.spring.core.warmup;

import pl.hit.spring.core.components.ConsolePrinter;
import pl.hit.spring.core.components.DialogPrinter;
import pl.hit.spring.core.components.FilePrinter;
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
        HelloWorld firstHello = new HelloWorld(new ConsolePrinter());
        firstHello.sayHello();

        HelloWorld secondHello = new HelloWorld(new FilePrinter());
        secondHello.sayHello();

        HelloWorld thirdHello = new HelloWorld(new DialogPrinter());
        thirdHello.sayHello();
    }
}
```