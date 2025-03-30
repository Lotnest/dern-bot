package dev.lotnest.dernbot.wynncraft.api.player;

import lombok.Builder;
import lombok.Data;

import java.util.Map;

@Data
@Builder
public class Raids {
    private int total;
    private Map<String, Integer> list;
}
