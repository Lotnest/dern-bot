package dev.lotnest.dernbot.wynncraft.api.player.association;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlayerAssociationDTO {
    private String name;
    private long count;
    private double probability;
}