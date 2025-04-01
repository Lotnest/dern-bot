package dev.lotnest.dernbot.wynncraft.api.player;

import dev.lotnest.dernbot.core.utils.MinecraftColor;
import dev.lotnest.dernbot.jda.utils.DateUtils;
import dev.lotnest.dernbot.wynncraft.api.guild.Guild;
import dev.lotnest.dernbot.wynncraft.api.guild.GuildApiService;
import dev.lotnest.dernbot.wynncraft.api.guild.Member;
import net.dv8tion.jda.api.EmbedBuilder;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface PlayerMapper {
    PlayerMapper INSTANCE = Mappers.getMapper(PlayerMapper.class);

    default EmbedBuilder mapPlayerToEmbedBuilder(Player player, Section section, GuildApiService guildApiService) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Stats - Player");
        embedBuilder.setColor(MinecraftColor.GREEN.getColor());
        embedBuilder.setThumbnail("https://visage.surgeplay.com/bust/100/" + player.getUuid());
        embedBuilder
                .getDescriptionBuilder()
                .append(player.isOnline() ? "\uD83D\uDFE2 " : "\uD83D\uDD34 ")
                .append("**")
                .append(player.getUsername())
                .append("**")
                .append(player.isOnline() ? " on **" + player.getServer() + "**" : "");

        Guild guild = null;
        if (player.getGuild() != null) {
            guild = guildApiService.getGuild(player.getGuild().getName());
        }

        return switch (section) {
            case GENERAL -> mapGeneralSection(
                    player,
                    embedBuilder,
                    guild != null ? guild.getMembers().getByUsername(player.getUsername()) : null);
            case RAIDS -> mapRaidsSection(player, embedBuilder);
            case DUNGEONS -> mapDungeonsSection(player, embedBuilder);
        };
    }

    private EmbedBuilder mapGeneralSection(Player player, EmbedBuilder embedBuilder, Member member) {
        embedBuilder
                .getDescriptionBuilder()
                .append(
                        """
                                
                                ### :arrow_forward: General
                                Guild: **""")
                .append(
                        player.getGuild() != null && player.getGuild().getUuid() != null
                                ? player.getGuild().getRank() + "** in **"
                                + player.getGuild().getName() + " ["
                                + player.getGuild().getPrefix() + "]**"
                                + (member != null
                                ? "\n(joined "
                                + DateUtils.formatDateToRelativeDiscordTimestamp(
                                member.getJoined())
                                + ")"
                                : "")
                                : "NONE**")
                .append("\nUUID: `")
                .append(player.getUuid())
                .append("`")
                .append("\nRank: **")
                .append(PlayerRank.fromString(player.getRankBadge()).name())
                .append("**")
                .append("\nFirst join: ")
                .append(DateUtils.formatDateToDiscordTimestamp(player.getFirstJoin()))
                .append(" (")
                .append(DateUtils.formatDateToRelativeDiscordTimestamp(player.getFirstJoin()))
                .append(")")
                .append("\nLast join: ")
                .append(DateUtils.formatDateToDiscordTimestamp(player.getLastJoin()))
                .append(" (")
                .append(DateUtils.formatDateToRelativeDiscordTimestamp(player.getLastJoin()))
                .append(")")
                .append("\nPlaytime: **")
                .append(player.getPlaytime())
                .append(" hours**")
                .append("\nWars: **")
                .append(player.getGlobalData().getWars())
                .append("**")
                .append("\nTotal level: **")
                .append(player.getGlobalData().getTotalLevel())
                .append("**")
                .append("\nKilled mobs: **")
                .append(player.getGlobalData().getKilledMobs())
                .append("**")
                .append("\nChests found: **")
                .append(player.getGlobalData().getChestsFound())
                .append("**")
                .append("\nCompleted quests: **")
                .append(player.getGlobalData().getCompletedQuests())
                .append("**");
        return embedBuilder;
    }

    private EmbedBuilder mapRaidsSection(Player player, EmbedBuilder embedBuilder) {
        embedBuilder
                .getDescriptionBuilder()
                .append(
                        """
                                
                                ### :arrow_forward: Raids
                                Total: **""")
                .append(player.getGlobalData().getRaids().getTotal())
                .append("**");

        for (Map.Entry<String, Integer> entry :
                player.getGlobalData().getRaids().getList().entrySet()) {
            embedBuilder
                    .getDescriptionBuilder()
                    .append("\n")
                    .append(entry.getKey())
                    .append(": **")
                    .append(entry.getValue())
                    .append("**");
        }

        return embedBuilder;
    }

    private EmbedBuilder mapDungeonsSection(Player player, EmbedBuilder embedBuilder) {
        embedBuilder
                .getDescriptionBuilder()
                .append(
                        """
                                
                                ### :arrow_forward: Dungeons
                                Total: **""")
                .append(player.getGlobalData().getDungeons().getTotal())
                .append("**");

        for (Map.Entry<String, Integer> entry :
                player.getGlobalData().getDungeons().getList().entrySet()) {
            embedBuilder
                    .getDescriptionBuilder()
                    .append("\n")
                    .append(entry.getKey())
                    .append(": **")
                    .append(entry.getValue())
                    .append("**");
        }

        return embedBuilder;
    }

    enum Section {
        GENERAL,
        RAIDS,
        DUNGEONS
    }
}
