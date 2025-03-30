package dev.lotnest.dernbot.wynncraft.api.player;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalData {
    private int wars;
    private int totalLevel;
    private int killedMobs;
    private int chestsFound;
    private Dungeons dungeons;
    private Raids raids;
    private int completedQuests;
}
