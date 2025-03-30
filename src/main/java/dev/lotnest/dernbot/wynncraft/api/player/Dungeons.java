package dev.lotnest.dernbot.wynncraft.api.player;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Dungeons {
    private int total;
    private Map<String, Integer> list;
}
