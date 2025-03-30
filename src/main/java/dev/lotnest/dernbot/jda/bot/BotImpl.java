package dev.lotnest.dernbot.jda.bot;

import com.google.common.base.Stopwatch;
import dev.lotnest.dernbot.core.heartbeat.Heartbeat;
import dev.lotnest.dernbot.core.utils.StoppableThread;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.sharding.DefaultShardManagerBuilder;
import net.dv8tion.jda.api.sharding.ShardManager;
import net.dv8tion.jda.api.utils.SessionControllerAdapter;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
@Slf4j
public class BotImpl implements Runnable, Bot {
    private final StoppableThread heartbeat;
    private final ShardManager shardManager;
    private final boolean[] connected;

    public BotImpl() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Starting the bot...");

        shardManager = DefaultShardManagerBuilder.createDefault(System.getenv("DISCORD_BOT_TOKEN"))
                .enableIntents(
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MEMBERS,
                        GatewayIntent.GUILD_MESSAGES,
                        GatewayIntent.MESSAGE_CONTENT
                )
                .setSessionController(new SessionControllerAdapter())
                .setShardsTotal(-1)
                .build();

        connected = new boolean[shardManager.getShardsTotal()];

        shardManager.setActivity(Activity.playing("Restarting..."));
        shardManager.setStatus(OnlineStatus.IDLE);

        awaitJDAReady();
        addEventListeners();

        shardManager.setActivity(Activity.playing("Corrupting %s servers".formatted(shardManager.getGuilds().size())));
        shardManager.setStatus(OnlineStatus.ONLINE);

        heartbeat = new Heartbeat(this);
        heartbeat.setName("dern-bot-heartbeat");

        log.info("Bot is online and connected to {} servers, took {} ms", shardManager.getGuilds().size(), stopwatch.stop().elapsed().toMillis());
    }

    @Override
    public Logger getLogger() {
        return log;
    }

    @Override
    public void shutdownHook() {
        Stopwatch stopwatch = Stopwatch.createStarted();
        log.info("Shutting down the bot...");

        shardManager.setStatus(OnlineStatus.IDLE);
        shardManager.setActivity(Activity.playing("Shutting down..."));
        shardManager.shutdown();
        heartbeat.terminate();

        log.info("Bot shutdown complete in {} ms", stopwatch.stop().elapsed().toMillis());
    }

    @Override
    public StoppableThread getHeartbeat() {
        return heartbeat;
    }

    @Override
    public ShardManager getShardManager() {
        return shardManager;
    }

    @Override
    public Integer getShardId(JDA jda) {
        return jda.getShardInfo().getShardId();
    }

    @Override
    public int getShardCount() {
        return shardManager.getShardsTotal();
    }

    @Override
    public void setShardConnected(int shardId, boolean connected) {
        if (shardId < 0 || shardId >= this.connected.length) {
            log.error("Invalid shard ID: {}", shardId);
            return;
        }

        boolean previous;
        synchronized (this) {
            previous = this.connected[shardId];
            this.connected[shardId] = connected;
        }

        if (previous != connected) {
            log.info("Shard {} {}", shardId, connected ? "went online" : "went offline");
        }
    }

    @Override
    public boolean isShardConnected(int shardId) {
        synchronized (this) {
            return shardId >= 0 && shardId < connected.length && connected[shardId];
        }
    }

    @Override
    public List<Integer> getConnectedShards() {
        synchronized (this) {
            return IntStream.range(0, connected.length)
                    .filter(i -> connected[i])
                    .boxed()
                    .toList();
        }
    }

    @Override
    public boolean areAllShardsConnected() {
        synchronized (this) {
            return IntStream.range(0, connected.length)
                    .allMatch(i -> connected[i]);
        }
    }

    @Override
    public void run() {
        log.info("Starting the bot heartbeat...");
        new Thread(heartbeat).start();
    }

    private void awaitJDAReady() {
        shardManager.getShards().forEach(jda -> {
            try {
                jda.awaitReady();
                log.info("JDA is ready for shard {}", jda.getShardInfo().getShardId());
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                log.error("Failed to await JDA ready for shard {}", jda.getShardInfo().getShardId(), exception);
            } finally {
                connected[jda.getShardInfo().getShardId()] = true;
            }
        });
        log.info("All shards are loaded and ready!");
    }

    private void addEventListeners() {
        shardManager.addEventListener();
    }
}
