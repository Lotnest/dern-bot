package dev.lotnest.dernbot.wynncraft.utils;

public class WynncraftUtils {
    private static final int[][] GUILD_LEVEL_MEMBER_SLOTS = {
        {0, 4},
        {2, 4},
        {6, 8},
        {15, 10},
        {24, 12},
        {33, 10},
        {42, 12},
        {54, 12},
        {66, 8},
        {75, 6},
        {81, 6},
        {87, 6},
        {93, 4},
        {96, 4},
        {99, 4},
        {102, 4},
        {105, 4},
        {108, 4},
        {111, 4},
        {114, 4},
        {117, 10},
        {120, 10}
    };

    private WynncraftUtils() {}

    public static int getMaxGuildMemberSlots(int guildLevel) {
        int totalSlots = 0;

        for (int[] levelSlot : GUILD_LEVEL_MEMBER_SLOTS) {
            int level = levelSlot[0];
            int value = levelSlot[1];

            if (guildLevel >= level) {
                totalSlots += value;
            } else {
                break;
            }
        }

        return totalSlots;
    }
}
