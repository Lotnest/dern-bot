package dev.lotnest.dernbot.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class BotMetrics {
    private final MeterRegistry registry;

    public void incrementStartupTimeMs(long durationMs) {
        registry.timer("bot.startup.timeMs").record(durationMs, TimeUnit.MILLISECONDS);
    }

    public void incrementShutdownTimeMs(long durationMs) {
        registry.timer("bot.shutdown.timeMs").record(durationMs, TimeUnit.MILLISECONDS);
    }
}
