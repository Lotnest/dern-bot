package dev.lotnest.dernbot.wynncraft.api.player;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Player {
    private String username;
    private boolean online;
    private String server;
    private String uuid;
    private String rankBadge;
    private String firstJoin;
    private String lastJoin;
    private double playtime;
    private Guild guild;
    private GlobalData globalData;
    private Map<String, Integer> ranking;
    private Map<String, Integer> previousRanking;
}
