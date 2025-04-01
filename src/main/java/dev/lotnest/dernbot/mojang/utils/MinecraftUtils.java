package dev.lotnest.dernbot.mojang.utils;

import java.util.regex.Pattern;

public final class MinecraftUtils {
    private static final Pattern UUID_PATTERN = Pattern.compile(
            "^[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$",
            Pattern.CASE_INSENSITIVE
    );
    private static final Pattern UUID_UNDASHED_PATTERN = Pattern.compile("^[0-9a-f]{32}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern USERNAME_PATTERN = Pattern.compile("^[a-z0-9_]{3,16}$", Pattern.CASE_INSENSITIVE);

    private MinecraftUtils() {}

    public static boolean isValidUUID(String uuid) {
        return UUID_PATTERN.matcher(uuid).matches() || UUID_UNDASHED_PATTERN.matcher(uuid).matches();
    }

    public static boolean isValidUsername(String username) {
        return USERNAME_PATTERN.matcher(username).matches();
    }
}
