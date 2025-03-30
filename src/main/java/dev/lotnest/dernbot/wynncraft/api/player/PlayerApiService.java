package dev.lotnest.dernbot.wynncraft.api.player;

import dev.lotnest.dernbot.core.http.clients.WynncraftApiHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PlayerApiService {
    private static final String PLAYER_API_URL = "https://api.wynncraft.com/v3/player/%s";

    private final WynncraftApiHttpClient wynncraftApiHttpClient;

    public Player getPlayer(String playerName) {
        return wynncraftApiHttpClient.getJson(String.format(PLAYER_API_URL, playerName), Player.class);
    }
}
