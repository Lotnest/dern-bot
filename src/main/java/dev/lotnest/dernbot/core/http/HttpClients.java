package dev.lotnest.dernbot.core.http;

import dev.lotnest.dernbot.core.http.clients.MojangApiHttpClient;
import dev.lotnest.dernbot.core.http.clients.WynncraftApiHttpClient;

public final class HttpClients {
    public static final WynncraftApiHttpClient WYNNCRAFT_API = WynncraftApiHttpClient.newHttpClient();
    public static final MojangApiHttpClient MOJANG_API = MojangApiHttpClient.newHttpClient();

    private HttpClients() {}
}
