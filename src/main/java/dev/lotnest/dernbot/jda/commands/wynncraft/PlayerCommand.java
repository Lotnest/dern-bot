package dev.lotnest.dernbot.jda.commands.wynncraft;

import com.github.kaktushose.jda.commands.annotations.interactions.Button;
import com.github.kaktushose.jda.commands.annotations.interactions.Command;
import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.Param;
import com.github.kaktushose.jda.commands.dispatching.events.interactions.CommandEvent;
import com.github.kaktushose.jda.commands.dispatching.events.interactions.ComponentEvent;
import com.github.kaktushose.jda.commands.dispatching.reply.Component;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import dev.lotnest.dernbot.core.utils.MinecraftColor;
import dev.lotnest.dernbot.mojang.utils.MinecraftUtils;
import dev.lotnest.dernbot.wynncraft.api.guild.GuildApiService;
import dev.lotnest.dernbot.wynncraft.api.player.Player;
import dev.lotnest.dernbot.wynncraft.api.player.PlayerApiService;
import dev.lotnest.dernbot.wynncraft.api.player.PlayerMapper;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
@Interaction
public class PlayerCommand {
    private static final String GENERAL_SECTION_BUTTON_LABEL = "General";
    private static final String RAIDS_SECTION_BUTTON_LABEL = "Raids";
    private static final String DUNGEONS_SECTION_BUTTON_LABEL = "Dungeons";

    private final Cache<String, Player> playerCache = CacheBuilder.newBuilder()
            .expireAfterWrite(15, TimeUnit.MINUTES)
            .build();

    private final PlayerApiService playerApiService;
    private final GuildApiService guildApiService;

    @Command(value = "player", desc = "Get player's profile.")
    public void playerCommand(CommandEvent event, @Param(name = "input", value = "Player's username or UUID.") String input) {
        if (StringUtils.isBlank(input) || (!MinecraftUtils.isValidUsername(input) && !MinecraftUtils.isValidUUID(input))) {
            event.with()
                    .reply(
                            new EmbedBuilder()
                                    .setColor(MinecraftColor.RED.getColor())
                                    .setTitle("Invalid input")
                                    .setDescription("Please provide a valid username or UUID.")
                    );
            return;
        }

        Player player = playerApiService.getPlayer(input);
        if (player == null) {
            event.with()
                    .reply(
                            new EmbedBuilder()
                                    .setColor(MinecraftColor.RED.getColor())
                                    .setTitle("Player not found")
                                    .setDescription("`" + input + "` has not played Wynncraft before.")
                    );
            return;
        }

        playerCache.put(
                event.with()
                        .components(
                                Component.disabled("generalButton"),
                                Component.enabled("raidsButton"),
                                Component.enabled("dungeonsButton"))
                        .reply(PlayerMapper.INSTANCE.mapPlayerToEmbedBuilder(player, PlayerMapper.Section.GENERAL, guildApiService))
                        .getId(),
                player);
    }

    @Button(value = GENERAL_SECTION_BUTTON_LABEL, style = ButtonStyle.PRIMARY)
    public void generalButton(ComponentEvent event) {
        Player cachedPlayer = playerCache.getIfPresent(event.jdaEvent().getMessageId());
        if (cachedPlayer == null) {
            return;
        }

        event.jdaEvent().editMessageEmbeds(
                        PlayerMapper.INSTANCE.mapPlayerToEmbedBuilder(cachedPlayer, PlayerMapper.Section.GENERAL, guildApiService).build()
                )
                .setActionRow(
                        event.jdaEvent().getMessage().getButtonsByLabel(GENERAL_SECTION_BUTTON_LABEL, false).getFirst().asDisabled(),
                        event.jdaEvent().getMessage().getButtonsByLabel(RAIDS_SECTION_BUTTON_LABEL, false).getFirst().asEnabled(),
                        event.jdaEvent().getMessage().getButtonsByLabel(DUNGEONS_SECTION_BUTTON_LABEL, false).getFirst().asEnabled()
                ).queue();
    }

    @Button(value = RAIDS_SECTION_BUTTON_LABEL, style = ButtonStyle.PRIMARY)
    public void raidsButton(ComponentEvent event) {
        Player cachedPlayer = playerCache.getIfPresent(event.jdaEvent().getMessageId());
        if (cachedPlayer == null) {
            return;
        }

        event.jdaEvent().editMessageEmbeds(
                        PlayerMapper.INSTANCE.mapPlayerToEmbedBuilder(cachedPlayer, PlayerMapper.Section.RAIDS, guildApiService).build()
                )
                .setActionRow(
                        event.jdaEvent().getMessage().getButtonsByLabel(GENERAL_SECTION_BUTTON_LABEL, false).getFirst().asEnabled(),
                        event.jdaEvent().getMessage().getButtonsByLabel(RAIDS_SECTION_BUTTON_LABEL, false).getFirst().asDisabled(),
                        event.jdaEvent().getMessage().getButtonsByLabel(DUNGEONS_SECTION_BUTTON_LABEL, false).getFirst().asEnabled()
                ).queue();
    }

    @Button(value = DUNGEONS_SECTION_BUTTON_LABEL, style = ButtonStyle.PRIMARY)
    public void dungeonsButton(ComponentEvent event) {
        Player cachedPlayer = playerCache.getIfPresent(event.jdaEvent().getMessageId());
        if (cachedPlayer == null) {
            return;
        }

        event.jdaEvent().editMessageEmbeds(
                        PlayerMapper.INSTANCE.mapPlayerToEmbedBuilder(cachedPlayer, PlayerMapper.Section.DUNGEONS, guildApiService).build()
                )
                .setActionRow(
                        event.jdaEvent().getMessage().getButtonsByLabel(GENERAL_SECTION_BUTTON_LABEL, false).getFirst().asEnabled(),
                        event.jdaEvent().getMessage().getButtonsByLabel(RAIDS_SECTION_BUTTON_LABEL, false).getFirst().asEnabled(),
                        event.jdaEvent().getMessage().getButtonsByLabel(DUNGEONS_SECTION_BUTTON_LABEL, false).getFirst().asDisabled()
                ).queue();
    }
}
