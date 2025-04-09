package dev.lotnest.dernbot.wynncraft.api.player.association;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
public class PlayerAssociationId implements Serializable {
    private String player1;
    private String player2;
    private String server;

    public PlayerAssociationId(String player1, String player2, String server) {
        if (player1.compareTo(player2) <= 0) {
            this.player1 = player1;
            this.player2 = player2;
        } else {
            this.player1 = player2;
            this.player2 = player1;
        }
        this.server = server;
    }
}
