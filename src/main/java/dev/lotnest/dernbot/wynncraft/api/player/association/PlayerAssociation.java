package dev.lotnest.dernbot.wynncraft.api.player.association;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Entity
@IdClass(PlayerAssociationId.class)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlayerAssociation {

    @Id
    private String player1;

    @Id
    private String player2;

    @Id
    private String server;

    private int count;

    public PlayerAssociation(String player1, String player2, String server, int count) {
        if (player1.compareTo(player2) <= 0) {
            this.player1 = player1;
            this.player2 = player2;
        } else {
            this.player1 = player2;
            this.player2 = player1;
        }
        this.server = server;
        this.count = count;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerAssociation)) return false;
        return new EqualsBuilder()
                .append(player1, ((PlayerAssociation) o).getPlayer1())
                .append(player2, ((PlayerAssociation) o).getPlayer2())
                .append(server, ((PlayerAssociation) o).getServer())
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder()
                .append(player1)
                .append(player2)
                .append(server)
                .toHashCode();
    }

    public void incrementCount() {
        this.count++;
    }
}
