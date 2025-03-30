package dev.lotnest.dernbot.jda.commands.wynncraft;

import com.github.kaktushose.jda.commands.annotations.interactions.Button;
import com.github.kaktushose.jda.commands.annotations.interactions.Command;
import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.Param;
import com.github.kaktushose.jda.commands.dispatching.events.interactions.CommandEvent;
import com.github.kaktushose.jda.commands.dispatching.events.interactions.ComponentEvent;
import com.github.kaktushose.jda.commands.dispatching.reply.Component;
import dev.lotnest.dernbot.wynncraft.api.guild.GuildApiService;
import dev.lotnest.dernbot.wynncraft.api.player.PlayerApiService;
import dev.lotnest.dernbot.wynncraft.api.player.PlayerMapper;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.interactions.components.buttons.ButtonStyle;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Interaction
public class PlayerCommand {
    private final PlayerApiService playerApiService;
    private final GuildApiService guildApiService;

    @Command(value = "player", desc = "Get player's profile.")
    public void playerCommand(CommandEvent event, @Param(name = "input", value = "Player's username or UUID.") String input) {
        event.with().components(
                        Component.disabled("generalButton"),
                        Component.enabled("raidsButton"),
                        Component.enabled("dungeonsButton"))
                .reply(PlayerMapper.INSTANCE.mapPlayerToEmbedBuilder(playerApiService.getPlayer(input), PlayerMapper.Section.GENERAL, guildApiService));
    }

    @Button(value = "General", style = ButtonStyle.PRIMARY)
    public void generalButton(ComponentEvent event) {

    }

    @Button(value = "Raids", style = ButtonStyle.PRIMARY)
    public void raidsButton(ComponentEvent event) {

    }

    @Button(value = "Dungeons", style = ButtonStyle.PRIMARY)
    public void dungeonsButton(ComponentEvent event) {

    }
}
