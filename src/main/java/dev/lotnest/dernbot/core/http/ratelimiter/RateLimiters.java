package dev.lotnest.dernbot.core.http.ratelimiter;

public final class RateLimiters {
    public static final RateLimiter WYNNCRAFT_API = new RateLimiter(5, 180);
    public static final RateLimiter MOJANG_API = new RateLimiter(5, 60);

    private RateLimiters() {
    }
}
