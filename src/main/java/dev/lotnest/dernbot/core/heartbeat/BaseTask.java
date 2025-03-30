package dev.lotnest.dernbot.core.heartbeat;

public interface BaseTask {
    String getName();
    void run();
    long getInitialDelayMs();
    long getIntervalMs();
}
