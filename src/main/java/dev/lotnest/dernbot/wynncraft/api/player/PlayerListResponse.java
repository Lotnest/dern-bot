package dev.lotnest.dernbot.wynncraft.api.player;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
public class PlayerListResponse {
    private int total;
    private Map<String, String> players;
}
