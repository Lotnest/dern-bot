package dev.lotnest.dernbot.wynncraft.api.player.association;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

@Table(name = "player_associations")
@Entity
@IdClass(PlayerAssociationId.class)
@NoArgsConstructor
@Getter
@Setter
@ToString
public class PlayerAssociation {
    @Id
    private String playerA;

    @Id
    private String playerB;

    @Id
    private String server;

    private int count;

    public PlayerAssociation(String playerA, String playerB, String server, int count) {
        if (playerA.compareTo(playerB) <= 0) {
            this.playerA = playerA;
            this.playerB = playerB;
        } else {
            this.playerA = playerB;
            this.playerB = playerA;
        }
        this.server = server;
        this.count = count;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PlayerAssociation)) return false;
        return new EqualsBuilder()
                .append(playerA, ((PlayerAssociation) o).getPlayerA())
                .append(playerB, ((PlayerAssociation) o).getPlayerB())
                .append(server, ((PlayerAssociation) o).getServer())
                .isEquals();
    }

    @Override
    public final int hashCode() {
        return new HashCodeBuilder()
                .append(playerA)
                .append(playerB)
                .append(server)
                .toHashCode();
    }

    public void incrementCount() {
        this.count++;
    }
}
