package pl.honestit.spring.core.warmup;

import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class HelloWorld {

    private AtomicInteger counter = new AtomicInteger(1);
    private ThreadLocal<Integer> value;

    public void sayHello() {
        System.out.println("Hello, world!" + counter.incrementAndGet());
    }
}
