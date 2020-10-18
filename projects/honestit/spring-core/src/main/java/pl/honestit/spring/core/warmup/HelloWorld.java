package pl.honestit.spring.core.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import pl.honestit.spring.core.components.Console;
import pl.honestit.spring.core.components.Printer;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;

@Component
public class HelloWorld {

    private Printer consolePrinter;
    private Printer dialogPrinter;
    private Printer defaultPrinter;

    private Collection<Printer> printers;

    public HelloWorld() {
    }

    @Autowired
    public HelloWorld(@Console Printer consolePrinter) {
        this.consolePrinter = consolePrinter;
    }

    @Autowired
    public void setDialogPrinter(@Qualifier("dialogPrinter") Printer dialogPrinter) {
        this.dialogPrinter = dialogPrinter;
    }

    public void sayHello() {
        String helloMsg = "Hello, world!";
        Integer r = new Random().nextInt(3);
        Optional.of(r).filter(v -> v.equals(0))
                .ifPresentOrElse(v -> consolePrinter.print(helloMsg),
                        () -> Optional.of(r).filter(v -> v.equals(1))
                                .ifPresentOrElse(
                                        v -> dialogPrinter.print(helloMsg),
                                        () -> defaultPrinter.print(helloMsg)));
    }

    @Autowired
    public void setDefaultPrinter(Printer defaultPrinter) {
        this.defaultPrinter = defaultPrinter;
    }

    @Autowired
    public void setPrinters(Collection<Printer> printers) {
        this.printers = printers;
    }

    public void printPrinters() {
        printers.stream().forEach(printer -> printer.print("Yupi ja jej!"));
    }
}
