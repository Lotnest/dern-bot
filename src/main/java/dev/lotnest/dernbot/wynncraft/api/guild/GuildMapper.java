package dev.lotnest.dernbot.wynncraft.api.guild;

import dev.lotnest.dernbot.core.utils.MinecraftColor;
import dev.lotnest.dernbot.jda.utils.DateUtils;
import dev.lotnest.dernbot.wynncraft.utils.WynncraftUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.Map;

@Mapper
public interface GuildMapper {
    GuildMapper INSTANCE = Mappers.getMapper(GuildMapper.class);

    default MessageEmbed mapGuildToMessageEmbed(Guild guild, boolean showOfflineMembers) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setTitle("Stats - Guild");
        embedBuilder.setColor(MinecraftColor.GREEN.getColor());

        if (showOfflineMembers) {
            embedBuilder.setDescription("**" + guild.getName() + " [" + guild.getPrefix()
                    + "]**\n\n`Showing all members, including those offline.`");
        }

        embedBuilder.addField("Level", guild.getLevel() + " (" + guild.getXpPercent() + "%)", true);
        embedBuilder.addField("Territories owned", String.valueOf(guild.getTerritories()), true);
        embedBuilder.addField("Total wars", String.valueOf(guild.getWars()), true);
        embedBuilder.addField(
                "Created",
                DateUtils.formatDateToDiscordTimestamp(guild.getCreated()) + " ("
                        + DateUtils.formatDateToRelativeDiscordTimestamp(guild.getCreated()) + ")",
                true);

        if (guild.getMembers() != null) {
            int totalMembers = guild.getMembers().getTotal();
            int maxSlots = WynncraftUtils.getMaxGuildMemberSlots(guild.getLevel());

            if (!showOfflineMembers) {
                int onlineMembers = calculateOnlineMembers(guild);
                embedBuilder.addField("Members", onlineMembers + "/" + maxSlots, true);
            } else {
                embedBuilder.addField("Members", totalMembers + "/" + maxSlots, true);
            }

            addMemberField(embedBuilder, "Owner", guild.getMembers().getOwner(), showOfflineMembers);
            addMemberField(embedBuilder, "Chief", guild.getMembers().getChief(), showOfflineMembers);
            addMemberField(embedBuilder, "Strategist", guild.getMembers().getStrategist(), showOfflineMembers);
            addMemberField(embedBuilder, "Captain", guild.getMembers().getCaptain(), showOfflineMembers);
            addMemberField(embedBuilder, "Recruiter", guild.getMembers().getRecruiter(), showOfflineMembers);
            addMemberField(embedBuilder, "Recruit", guild.getMembers().getRecruit(), showOfflineMembers);
        }

        return embedBuilder.build();
    }

    default int calculateOnlineMembers(Guild guild) {
        int onlineCount = 0;

        onlineCount += countOnlineMembers(guild.getMembers().getOwner());
        onlineCount += countOnlineMembers(guild.getMembers().getChief());
        onlineCount += countOnlineMembers(guild.getMembers().getStrategist());
        onlineCount += countOnlineMembers(guild.getMembers().getCaptain());
        onlineCount += countOnlineMembers(guild.getMembers().getRecruiter());
        onlineCount += countOnlineMembers(guild.getMembers().getRecruit());

        return onlineCount;
    }

    default <T extends Member> int countOnlineMembers(Map<String, T> members) {
        if (members == null || members.isEmpty()) {
            return 0;
        }

        return (int) members.values().stream().filter(Member::isOnline).count();
    }

    default <T extends Member> void addMemberField(
            EmbedBuilder embedBuilder, String fieldName, Map<String, T> members, boolean showOfflineMembers) {
        if (members == null || members.isEmpty()) {
            return;
        }

        StringBuilder builder = new StringBuilder("`");
        boolean hasVisibleMembers = false;

        for (Map.Entry<String, T> entry : members.entrySet()) {
            String name = entry.getKey();
            T member = entry.getValue();

            if (!showOfflineMembers && !member.isOnline()) {
                continue;
            }

            builder.append(name)
                    .append(member.getServer() != null ? " (" + member.getServer() + ")" : "")
                    .append(", ");

            hasVisibleMembers = true;
        }

        if (!hasVisibleMembers) {
            builder.append("-");
        } else if (builder.length() > 1) {
            builder.setLength(builder.length() - 2);
        }

        builder.append("`");

        embedBuilder.addField(fieldName, builder.toString(), false);
    }
}
