package dev.lotnest.dernbot.wynncraft.api.player;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Guild {
    private String uuid;
    private String name;
    private String prefix;
    private String rank;
    private String rankStars;
}
