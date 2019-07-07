package ru.javawebinar.topjava.rules;

import org.junit.rules.TestWatcher;
import org.junit.runner.Description;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;


public class TestTimeWatcher extends TestWatcher {
    private static Long totalDuration = 0L;
    private static final Logger logger = LoggerFactory.getLogger(TestTimeWatcher.class);
    private Instant start;

    @Override
    public void starting(Description desc) {
        start = Instant.now();
    }

    @Override
    public void finished(Description desc) {
        final long duration = Duration.between(start, Instant.now()).toMillis();
        totalDuration += duration;
                logger.info(desc.getMethodName() + " COMPLETED.  EXECUTION TIME: "
                + duration + " ms");
    }

    public static void logTotalAndRefresh(Class<?> clazz) {
        logger.info(clazz.getSimpleName() + " COMPLETED. TOTAL TIME: " + totalDuration + " ms");
        totalDuration = 0L;
    }
}
