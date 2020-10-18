package pl.honestit.spring.core.warmup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.honestit.spring.core.components.ConsolePrinter;
import pl.honestit.spring.core.components.DialogPrinter;
import pl.honestit.spring.core.components.Printer;

import java.util.Optional;
import java.util.Random;

@Component
public class HelloWorld {

    private ConsolePrinter consolePrinter;
    private DialogPrinter dialogPrinter;
    private Printer defaultPrinter;

    public HelloWorld() {
    }

    @Autowired
    public HelloWorld(ConsolePrinter consolePrinter) {
        this.consolePrinter = consolePrinter;
    }

    @Autowired
    public void setDialogPrinter(DialogPrinter dialogPrinter) {
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
}
