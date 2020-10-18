package pl.honestit.spring.core.components;

import org.springframework.stereotype.Component;

@Component @Console
public class ConsolePrinter implements Printer {

    @Override
    public void print(String message) {
        System.out.println(message);
    }
}
