Implementacja wzorca Singleton z perspektywy fabryki `Printer`. Nie korzystamy z sztywnej implementacji wzoraca Singleton na poziomie klas konkretnych, ale w znaczeniu efektywnego Singleton'a na poziomie klasy fabryki. Tworzymy pule obiektów, a w póli tej każdy obiekt występuje jako jedna sztuka.

```java
package pl.hit.spring.core.warmup;

import pl.hit.spring.core.components.ConsolePrinter;
import pl.hit.spring.core.components.DialogPrinter;
import pl.hit.spring.core.components.FilePrinter;
import pl.hit.spring.core.components.Printer;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Printers {

    private static final Map<String, Printer> singletonsPool;

    static {
        Map<String, Printer> builderMap = new HashMap<>();
        builderMap.put("consolePrinter", new ConsolePrinter(System.out));
        builderMap.put("errorConsolePrinter", new ConsolePrinter(System.err));
        builderMap.put("filePrinter", new FilePrinter());
        builderMap.put("dialogPrinter", new DialogPrinter());
        
        singletonsPool = Collections.unmodifiableMap(builderMap);
    }

    public static Printer consolePrinter() {
        return singletonsPool.get("consolePrinter");
    }

    public static Printer errorConsolePrinter() {
        return singletonsPool.get("errorConsolePrinter");
    }

    public static Printer filePrinter() {
        return singletonsPool.get("filePrinter");
    }

    public static Printer dialogPrinter() {
        return singletonsPool.get("dialogPrinter");
    }
}
```

---

Kod klasy `LoggablePrinter`, która staje się Decorator'em dla pozostałych klas typu `Printer`:

```java
package pl.hit.spring.core.components;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;

public class LoggablePrinter implements Printer {

    private final Printer decorated;
    private final Path auditPath;

    public LoggablePrinter(Printer decorated, Path auditPath) {
        this.decorated = decorated;
        this.auditPath = auditPath;
    }

    @Override
    public void print(String message) {
        log(message);
        decorated.print(message);
    }

    private void log(String message) {
        try {
            Files.write(auditPath, Arrays.asList(message), StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
```

---

Na koniec wykorzystanie Decorator'a w fabryce `Printers`, aby wiedzieć co jest zapisywane w aplikacji:

```java
package pl.hit.spring.core.warmup;

import pl.hit.spring.core.components.*;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Printers {

    private static final Map<String, Printer> singletonsPool;

    static {
        Map<String, Printer> builderMap = new HashMap<>();
        builderMap.put("consolePrinter", new ConsolePrinter(System.out));
        builderMap.put("errorConsolePrinter", new ConsolePrinter(System.err));
        builderMap.put("filePrinter", new FilePrinter());
        builderMap.put("dialogPrinter", new DialogPrinter());

        singletonsPool = Collections.unmodifiableMap(builderMap);
    }

    public static Printer consolePrinter() {
        return loggable(singletonsPool.get("consolePrinter"));
    }

    public static Printer errorConsolePrinter() {
        return loggable(singletonsPool.get("errorConsolePrinter"))
    }

    public static Printer filePrinter() {
        return loggable(singletonsPool.get("filePrinter"));
    }

    public static Printer dialogPrinter() {
        return loggable(singletonsPool.get("dialogPrinter"));
    }

    private static Printer loggable(Printer printer) {
        Path logPath = Paths.get(System.getProperty("user.home"), "audit.log");
        return new LoggablePrinter(printer, logPath);
    }
}
```