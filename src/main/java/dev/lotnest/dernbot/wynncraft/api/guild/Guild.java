package dev.lotnest.dernbot.wynncraft.api.guild;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Guild {
    private String uuid;
    private String name;
    private String prefix;
    private int level;
    private int xpPercent;
    private int territories;
    private long wars;
    private String created;
    private Members members;
}
