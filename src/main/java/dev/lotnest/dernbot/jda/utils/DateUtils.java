package dev.lotnest.dernbot.jda.utils;

import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

public class DateUtils {
    private DateUtils() {}

    /**
     * Get a creation time in long (epoch, milliseconds from 1970/01/01 00:00:00.000 UTC) from discord IDs such as
     *  user / channel / guild / message IDs.
     * Discord epoch (first second of 2014): 1,420,070,400,000 (from discordapi.com)
     * Returns (discordId >> 22) + 1,420,070,400,000.
     * @param discordId Discord ID.
     * @return Time in epoch milliseconds.
     */
    public static long getIdCreationTime(long discordId) {
        return (discordId >> 22) + 1420070400000L;
    }

    private static long getEpochSeconds(String dateString) {
        Instant instant = Instant.parse(dateString);
        return instant.getEpochSecond();
    }

    public static String formatDateToDiscordTimestamp(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return "";
        }
        return "<t:" + getEpochSeconds(dateString) + ">";
    }

    public static String formatDateToRelativeDiscordTimestamp(String dateString) {
        if (StringUtils.isBlank(dateString)) {
            return "";
        }
        return "<t:" + getEpochSeconds(dateString) + ":R>";
    }
}
