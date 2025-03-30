package dev.lotnest.dernbot.jda.bot;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.sharding.ShardManager;
import org.slf4j.Logger;

import java.util.List;

public interface Bot {
    Logger getLogger();
    void run();
    void shutdownHook();

    ShardManager getShardManager();
    Integer getShardId(JDA jda);
    int getShardCount();
    void setShardConnected(int shardId, boolean connected);
    boolean isShardConnected(int shardId);
    List<Integer> getConnectedShards();
    boolean areAllShardsConnected();
}
