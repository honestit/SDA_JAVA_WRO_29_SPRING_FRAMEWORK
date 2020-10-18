package pl.honestit.spring.core.config;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.honestit.spring.core.components.Console;
import pl.honestit.spring.core.components.Printer;
import pl.honestit.spring.core.warmup.HelloWorld;

@Configuration
@ComponentScan(basePackages = "pl.honestit.spring.core")
public class WarmUpConfiguration {

    @Bean
    public HelloWorld consoleHelloWorld(@Console Printer defaulPrinter) {
        return new HelloWorld(defaulPrinter);
    }

    @Bean
    public HelloWorld dialogHelloWorld(@Qualifier("dialogPrinter") Printer printer) {
        return new HelloWorld(printer);
    }
}
