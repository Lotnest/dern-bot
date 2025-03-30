package dev.lotnest.dernbot.core.heartbeat;

import com.google.common.base.Stopwatch;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

@Slf4j
@RequiredArgsConstructor
public class HeartbeatTask {
    private final Timer timer = new Timer();
    private final BaseTask baseTask;

    public void start() {
        timer.schedule(getTimerTask(), baseTask.getInitialDelayMs());
    }

    public void stop() {
        timer.cancel();
    }

    private TimerTask getTimerTask() {
        HeartbeatTask heartBeatTask = this;
        return new TimerTask() {
            @Override
            public void run() {
                Stopwatch stopwatch = Stopwatch.createStarted();
                try {
                    heartBeatTask.baseTask.run();
                } catch (Exception exception) {
                    log.error("HeatBeat: {}: caught exception", heartBeatTask.baseTask.getName(), exception);
                }
                log.info("HeartBeat: {}: took {} ms", heartBeatTask.baseTask.getName(), stopwatch.stop().elapsed().toMillis());
                heartBeatTask.reschedule();
            }
        };
    }

    private void reschedule() {
        timer.schedule(getTimerTask(), baseTask.getIntervalMs());
    }
}
