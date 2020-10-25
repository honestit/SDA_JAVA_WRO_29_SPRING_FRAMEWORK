package pl.honestit.spring.web.app.session;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
@Scope(scopeName = "session", proxyMode = ScopedProxyMode.TARGET_CLASS)
public class UserIdGenerator {

    private AtomicLong nextId = new AtomicLong(1L);

    public Long getNextId() {
        return nextId.getAndIncrement();
    }
}
