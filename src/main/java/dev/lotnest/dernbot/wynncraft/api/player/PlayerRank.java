package dev.lotnest.dernbot.wynncraft.api.player;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@RequiredArgsConstructor
@Getter
public enum PlayerRank {
    NONE("", ""),
    // Normal ranks
    VIP("VIP", "\uE023"),
    VIP_PLUS("VIP+", "\uE024"),
    HERO("Hero", "\uE01B"),
    CHAMPION("CHAMPION", "\uE017"),
    MEDIA("Media", "\uE01E"),
    // CT ranks (all have same colors)
    ARTIST("Artist", "\uE015"),
    BUILDER("Builder", "\uE016"),
    CMD("CMD", "\uE018"),
    GAMEMASTER("GameMaster", "\uE01A"),
    HYBRID("Hybrid", "\uE01C"),
    ITEM("Item", "\uE01D"),
    MUSIC("Music", "\uE020"),
    QA("QA", "\uE022"),
    // Staff ranks
    MODERATOR("Moderator", "\uE01F"),
    WEBDEV("WebDev", "\uE025"),
    DEV("Dev", "\uE019"),
    ADMINISTRATOR("Administrator", "\uE014"),
    OWNER("Owner", "\uE021");

    private final String name;
    private final String tag;

    public static PlayerRank fromString(String rankString) {
        if (StringUtils.isBlank(rankString)) {
            return NONE;
        }

        if (StringUtils.startsWithIgnoreCase(rankString, "nextgen/badges/rank_")) {
            rankString = StringUtils.replaceIgnoreCase(rankString, "nextgen/badges/rank_", "");
            rankString = StringUtils.replaceIgnoreCase(rankString, ".svg", "");
        }

        for (PlayerRank rank : values()) {
            if (StringUtils.equalsIgnoreCase(rank.name, rankString)
                    || StringUtils.equalsIgnoreCase(rank.tag, rankString)) {
                return rank;
            }
        }

        return NONE;
    }
}
