package dev.lotnest.dernbot.core.heartbeat;

import com.google.common.collect.Lists;
import dev.lotnest.dernbot.core.utils.StoppableThread;
import dev.lotnest.dernbot.jda.bot.Bot;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class Heartbeat extends StoppableThread {
    private final Bot bot;
    private final List<HeartbeatTask> tasks = Lists.newArrayList();

    @Override
    public void run() {
        log.info("Starting heartbeat... (Thread id {})", this.threadId());
        tasks.forEach(HeartbeatTask::start);
    }

    @Override
    protected void cleanUp() {
        log.info("Stopping heartbeat... (Thread id {})", this.threadId());
        tasks.forEach(HeartbeatTask::stop);
    }

    public void addTask(BaseTask baseTask) {
        tasks.add(new HeartbeatTask(baseTask));
    }
}
