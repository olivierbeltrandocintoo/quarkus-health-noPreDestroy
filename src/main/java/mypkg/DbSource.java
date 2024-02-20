package mypkg;

import java.util.concurrent.atomic.AtomicInteger;

import io.quarkus.logging.Log;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.RequestScoped;

@RequestScoped
public class DbSource {
    static private final AtomicInteger counter = new AtomicInteger(2);
    private boolean isAcquired = false;

    public void acquire() {
        Log.info("Acquire...");
        var maxAttempts = 3;
        for (int i = 0; i < maxAttempts; i++) {
            if (counter.decrementAndGet() < 0) {
                counter.incrementAndGet();
                try {
                    Thread.sleep(1000);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                continue;
            }
            isAcquired = true;
            Log.info("Acquire OK!" + counter.get());
            return;
        }
        throw new RuntimeException("Could not acquire on time");
    }

    public void release() {
        Log.info("Release");
        if (!isAcquired)
            throw new RuntimeException("Bad release");
        isAcquired = false;
        counter.incrementAndGet();
    }

    // SUPER IMPORTANT: called by Quarkus when the scope ends (request scope)
    @PreDestroy
    void endOfRequest() {
        Log.warn("endOfRequest called");
        if (isAcquired)
            release();
    }

}
