package dev.lotnest.dernbot.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class CommandMetrics {
    private static final String COMMAND_TAG = "command";

    private final MeterRegistry meterRegistry;

    public void incrementCommandUsage(String commandName) {
        meterRegistry.counter(COMMAND_TAG + ".usage", COMMAND_TAG, commandName).increment();
    }

    public void incrementCommandError(String commandName) {
        meterRegistry.counter(COMMAND_TAG + ".error", COMMAND_TAG, commandName).increment();
    }

    public void incrementCommandExecutionTimeMs(String commandName, long durationMs) {
        meterRegistry.timer(COMMAND_TAG + ".execution.timeMs", COMMAND_TAG, commandName).record(durationMs, TimeUnit.MILLISECONDS);
    }
}
