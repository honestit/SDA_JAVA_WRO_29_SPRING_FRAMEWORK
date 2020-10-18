package pl.honestit.spring.core.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import pl.honestit.spring.core.warmup.HelloWorld;

@Configuration
@ComponentScan(basePackages = "pl.honestit.spring.core")
public class WarmUpConfiguration {

    @Bean
    public HelloWorld helloBean() {
        return new HelloWorld();
    }

    @Bean
    public HelloWorld buzzBuzz() {
        return new HelloWorld();
    }
}
