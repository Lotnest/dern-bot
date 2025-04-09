package dev.lotnest.dernbot.jda.commands.wynncraft;

import com.github.kaktushose.jda.commands.annotations.constraints.Max;
import com.github.kaktushose.jda.commands.annotations.constraints.Min;
import com.github.kaktushose.jda.commands.annotations.interactions.Command;
import com.github.kaktushose.jda.commands.annotations.interactions.Interaction;
import com.github.kaktushose.jda.commands.annotations.interactions.Optional;
import com.github.kaktushose.jda.commands.annotations.interactions.Param;
import com.github.kaktushose.jda.commands.dispatching.events.interactions.CommandEvent;
import com.google.common.collect.Lists;
import dev.lotnest.dernbot.core.utils.MinecraftColor;
import dev.lotnest.dernbot.mojang.utils.MinecraftUtils;
import dev.lotnest.dernbot.wynncraft.api.player.association.AssociationLookupService;
import dev.lotnest.dernbot.wynncraft.api.player.association.PlayerAssociationDTO;
import lombok.RequiredArgsConstructor;
import net.dv8tion.jda.api.EmbedBuilder;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Interaction
public class AssociationsCommand {
    private final AssociationLookupService associationLookupService;

    @Command(value = "associations", desc = "Get player's associations, used for looking up possible alts.")
    public void associationsCommand(CommandEvent event,
                                    @Param(name = "input", value = "Player's username or UUID.") String input,
                                    @Param(name = "min_probability", value = "Minimum probability (1-99)")
                                    @Optional("30") @Min(1) @Max(99) int minProbability) {
        if (StringUtils.isBlank(input) || (!MinecraftUtils.isValidUsername(input) && !MinecraftUtils.isValidUUID(input))) {
            event.reply(new EmbedBuilder()
                    .setColor(MinecraftColor.RED.getColor())
                    .setTitle("Invalid input")
                    .setDescription("Please provide a valid username or UUID."));
            return;
        }

        List<PlayerAssociationDTO> playerAssociations = associationLookupService.lookupAssociations(input, minProbability);
        if (playerAssociations.isEmpty()) {
            event.reply(new EmbedBuilder()
                    .setColor(MinecraftColor.RED.getColor())
                    .setTitle("No associations found")
                    .setDescription("No associations were found for `" + input + "` with a probability of **" + minProbability + "%**.\nPerhaps, try again with a lower probability?")
                    .setThumbnail("https://visage.surgeplay.com/bust/100/" + input));
            return;
        }

        List<EmbedBuilder> pages = Lists.newArrayList();
        EmbedBuilder embedBuilder = new EmbedBuilder()
                .setColor(MinecraftColor.GREEN.getColor())
                .setTitle("Associations for " + input + " (" + playerAssociations.size() + ") | " + minProbability + "%+")
                .setDescription("Below are players associated with `" + input + "`. Please note that this is not definitive and may not be accurate.")
                .setThumbnail("https://visage.surgeplay.com/bust/100/" + input);

        int fieldCount = 0;
        for (PlayerAssociationDTO association : playerAssociations) {
            if (fieldCount >= 25) {
                pages.add(embedBuilder);
                embedBuilder = new EmbedBuilder()
                        .setColor(MinecraftColor.GREEN.getColor())
                        .setTitle("Associations for " + input + " (" + playerAssociations.size() + ") | " + minProbability + "%+")
                        .setDescription("Below are players associated with `" + input + "`. Please note that this is not definitive and may not be accurate.")
                        .setThumbnail("https://visage.surgeplay.com/bust/100/" + input);
                fieldCount = 0;
            }

            embedBuilder.addField(
                    association.getName() + " (" + association.getProbability() + "%)",
                    "",
                    true
            );
            fieldCount++;
        }

        if (fieldCount > 0) {
            pages.add(embedBuilder);
        }

        event.reply(pages.getFirst());

        for (int i = 1; i < pages.size(); i++) {
            event.jdaEvent()
                    .getHook()
                    .sendMessageEmbeds(pages.get(i).build())
                    .queue();
        }
    }
}
