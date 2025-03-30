package dev.lotnest.dernbot.wynncraft.api.guild;

import dev.lotnest.dernbot.core.http.clients.WynncraftApiHttpClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GuildApiService {
    private static final String GUILD_API_URL = "https://api.wynncraft.com/v3/guild/%s";

    private final WynncraftApiHttpClient wynncraftApiHttpClient;

    public Guild getGuild(String guildName) {
        return wynncraftApiHttpClient.getJson(String.format(GUILD_API_URL, guildName), Guild.class);
    }
}
